from copy import deepcopy
from pathlib import Path

from docx import Document
from docx.enum.table import WD_ALIGN_VERTICAL
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml import OxmlElement
from docx.oxml.ns import qn
from docx.shared import Cm, Pt


ROOT = Path(__file__).resolve().parents[1]
WORKSPACE = ROOT.parent
DOCS_DIR = ROOT / "docs"
OUT_PATH = DOCS_DIR / "实验报告模板填写-血压血糖白盒测试部分.docx"


def find_template() -> Path:
    candidates = [
        p for p in WORKSPACE.glob("*.docx")
        if "实验报告" in p.name and "模板" in p.name and p.stat().st_size > 50_000
    ]
    if not candidates:
        raise FileNotFoundError("未找到实验报告模板.docx")
    return candidates[0]


def set_run_font(run, size=10.5, bold=False):
    run.font.name = "Times New Roman"
    if run._element.rPr is None:
        run._element.get_or_add_rPr()
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "宋体")
    run._element.rPr.rFonts.set(qn("w:ascii"), "Times New Roman")
    run._element.rPr.rFonts.set(qn("w:hAnsi"), "Times New Roman")
    run.font.size = Pt(size)
    run.bold = bold


def format_paragraph(paragraph, first_line=True):
    paragraph.paragraph_format.line_spacing = 1.25
    paragraph.paragraph_format.space_before = Pt(0)
    paragraph.paragraph_format.space_after = Pt(0)
    if first_line:
        paragraph.paragraph_format.first_line_indent = Cm(0.74)
    else:
        paragraph.paragraph_format.first_line_indent = None
    for run in paragraph.runs:
        set_run_font(run)


def replace_paragraph(paragraph, text, first_line=True):
    paragraph.text = ""
    format_paragraph(paragraph, first_line=first_line)
    if text:
        run = paragraph.add_run(text)
        set_run_font(run)
    return paragraph


def clear_paragraph(paragraph):
    paragraph.text = ""
    paragraph.paragraph_format.space_after = Pt(0)
    paragraph.paragraph_format.space_before = Pt(0)


def paragraph_after(paragraph, text="", first_line=True):
    new_p = OxmlElement("w:p")
    paragraph._p.addnext(new_p)
    p = paragraph._parent.add_paragraph()
    p._p.getparent().remove(p._p)
    new_paragraph = paragraph._parent.paragraphs[-1]
    # The list cache is not updated reliably after manual insertion, so wrap via document XML.
    from docx.text.paragraph import Paragraph
    inserted = Paragraph(new_p, paragraph._parent)
    if paragraph.style:
        inserted.style = paragraph.style
    replace_paragraph(inserted, text, first_line=first_line)
    return inserted


def insert_after(block, element):
    block._element.addnext(element)


def set_cell_text(cell, text, bold=False, center=False):
    cell.vertical_alignment = WD_ALIGN_VERTICAL.CENTER
    cell.text = ""
    p = cell.paragraphs[0]
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER if center else WD_ALIGN_PARAGRAPH.LEFT
    p.paragraph_format.line_spacing = 1.25
    p.paragraph_format.first_line_indent = None
    p.paragraph_format.space_before = Pt(0)
    p.paragraph_format.space_after = Pt(0)
    run = p.add_run(text)
    set_run_font(run, size=9, bold=bold)


def shade_cell(cell, fill):
    tc_pr = cell._tc.get_or_add_tcPr()
    shd = tc_pr.find(qn("w:shd"))
    if shd is None:
        shd = OxmlElement("w:shd")
        tc_pr.append(shd)
    shd.set(qn("w:fill"), fill)


def set_table_borders(table):
    tbl_pr = table._tbl.tblPr
    borders = tbl_pr.first_child_found_in("w:tblBorders")
    if borders is None:
        borders = OxmlElement("w:tblBorders")
        tbl_pr.append(borders)
    for edge in ("top", "left", "bottom", "right", "insideH", "insideV"):
        tag = "w:{}".format(edge)
        element = borders.find(qn(tag))
        if element is None:
            element = OxmlElement(tag)
            borders.append(element)
        element.set(qn("w:val"), "single")
        element.set(qn("w:sz"), "4")
        element.set(qn("w:space"), "0")
        element.set(qn("w:color"), "000000")


def add_table_after(doc, paragraph, headers, rows, widths_cm):
    table = doc.add_table(rows=1, cols=len(headers))
    table.autofit = False
    set_table_borders(table)

    for i, header in enumerate(headers):
        cell = table.rows[0].cells[i]
        cell.width = Cm(widths_cm[i])
        shade_cell(cell, "D9EAF7")
        set_cell_text(cell, header, bold=True, center=True)

    for row in rows:
        cells = table.add_row().cells
        for i, value in enumerate(row):
            cells[i].width = Cm(widths_cm[i])
            set_cell_text(cells[i], str(value), center=i == 0)

    insert_after(paragraph, table._tbl)
    return table


def paragraphs_by_text(doc):
    return {p.text.strip(): p for p in doc.paragraphs if p.text.strip()}


def find_paragraph(doc, predicate, occurrence=0):
    matches = [p for p in doc.paragraphs if predicate(p.text.strip())]
    if len(matches) <= occurrence:
        raise ValueError("未找到匹配段落")
    return matches[occurrence]


def main():
    DOCS_DIR.mkdir(exist_ok=True)
    template = find_template()
    doc = Document(template)
    lookup = paragraphs_by_text(doc)

    replace_paragraph(
        lookup["将本组开发的程序的基本情况进行介绍，包括面向的用户群，具有哪些功能。"],
        "本组开发的程序为老年人健康管理助手，面向需要记录和评估日常健康指标的老年用户及其家属。"
        "系统包含血压评估、血糖评估、睡眠评估、运动建议、用药提醒和 AI 健康问答等功能。本人负责其中的血压评估和血糖评估模块："
        "血压评估根据收缩压、舒张压判断正常、偏高或高血压；血糖评估根据血糖值和测量时段判断低血糖、正常、偏高或高血糖，并给出健康建议。",
    )

    replace_paragraph(
        find_paragraph(doc, lambda text: text.startswith("实验报告中的测试计划仅仅是测试计划中的一部分内容"), 0),
        "本次白盒测试的目标是验证血压评估与血糖评估两个业务逻辑模块的内部判断逻辑是否正确，重点检查合法性校验、条件判断、分支选择、"
        "边界值处理和返回结果封装。通过 JUnit 单元测试确认正常路径、异常路径和关键边界路径均能得到预期结果。",
    )
    for text in ["注意说明文字需要删除，不能保留在文档中。", "如果包含图片，图片要居中，而且图片的高度不能超过页面高度的1/3。"]:
        if text in lookup:
            clear_paragraph(lookup[text])

    scope_heading = lookup["（3）测试范围"]
    paragraph_after(
        scope_heading,
        "测试范围包括 com.healthassistant.logic.BpEvaluator 和 com.healthassistant.logic.BgEvaluator 两个类。"
        "血压评估测试覆盖收缩压、舒张压的非正数校验、超范围校验、正常、偏高和高血压分支；"
        "血糖评估测试覆盖血糖值校验、测量时段校验、空腹/餐后两套判断标准，以及低血糖、正常、偏高和高血糖分支。"
        "JavaFX 界面交互、睡眠评估、运动建议、用药提醒和 AI 健康问答不属于本人本次白盒测试范围。",
    )

    env_heading = lookup["（4）测试环境"]
    paragraph_after(
        env_heading,
        "操作系统为 Windows，开发语言为 Java 17，构建工具为 Maven，测试框架为 JUnit 5，测试执行命令为 mvn test。"
        "测试代码位于 src/test/java/com/healthassistant/logic 目录下。",
    )

    tool_heading = lookup["（5）测试工具"]
    paragraph_after(
        tool_heading,
        "本次白盒测试使用 JUnit 5 编写单元测试，使用 Maven Surefire 插件统一执行测试。"
        "新增测试文件为 BpEvaluatorWhiteBoxTest.java 和 BgEvaluatorWhiteBoxTest.java。",
    )

    strategy_heading = lookup["（6）测试策略"]
    paragraph_after(
        strategy_heading,
        "本次测试采用语句覆盖、分支覆盖/判定覆盖、条件覆盖、主要路径覆盖和边界值测试相结合的策略。"
        "其中，血压模块重点覆盖 systolic <= 0 || diastolic <= 0、systolic > 300 || diastolic > 200、"
        "systolic < 120 && diastolic < 80、systolic < 140 && diastolic < 90 等条件；"
        "血糖模块重点覆盖 value <= 0、value > 35、测量时段是否合法、空腹/餐后区间选择以及各等级判断边界。",
    )

    # Mark the parts outside the user's responsibility without leaving template instructions.
    exp2_target = find_paragraph(doc, lambda text: text.startswith("实验报告中的测试计划仅仅是测试计划中的一部分内容"), 0)
    replace_paragraph(exp2_target, "实验2黑盒测试计划由负责界面和手工测试的组员补充。")

    case_intro = find_paragraph(doc, lambda text: text.startswith("本部分的内容需要列出采用的测试方法"), 0)
    replace_paragraph(
        case_intro,
        "本部分采用 JUnit 5 对血压评估和血糖评估模块进行白盒测试。测试方法包括语句覆盖、分支覆盖/判定覆盖、条件覆盖、主要路径覆盖和边界值测试。"
        "具体测试用例如下表所示。",
    )
    add_table_after(
        doc,
        case_intro,
        ["编号", "测试对象", "输入数据", "白盒覆盖目标", "预期结果"],
        [
            ["BP-WB-01", "血压评估", "119.9 / 79.9", "正常路径、严格上界", "等级为正常，返回输入值和建议"],
            ["BP-WB-02", "血压评估", "120 / 79；119 / 80；139.9 / 89.9", "偏高路径、复合条件覆盖", "等级为偏高"],
            ["BP-WB-03", "血压评估", "140 / 89；139 / 90；160 / 100", "高血压路径、复合条件覆盖", "等级为高血压"],
            ["BP-WB-04", "血压评估", "0 / 80；120 / 0；-1 / -1", "非正数异常路径", "抛出 IllegalArgumentException"],
            ["BP-WB-05", "血压评估", "301 / 80；120 / 201；301 / 201", "超范围异常路径", "抛出 IllegalArgumentException"],
            ["BP-WB-06", "血压评估", "300 / 200", "最大合法值路径", "等级为高血压，返回字段完整"],
            ["BG-WB-01", "血糖评估", "空腹：3.8、3.9、6.1、6.1001、7.0、7.0001", "空腹区间选择、边界值", "覆盖低血糖、正常、偏高、高血糖"],
            ["BG-WB-02", "血糖评估", "餐后：3.8、3.9、7.8、7.8001、11.1、11.1001", "餐后区间选择、边界值", "覆盖低血糖、正常、偏高、高血糖"],
            ["BG-WB-03", "血糖评估", "6.2 + 空腹/餐后", "相同数值不同时段路径", "空腹为偏高，餐后为正常"],
            ["BG-WB-04", "血糖评估", "0；-0.1；35.1", "非法血糖值异常路径", "抛出 IllegalArgumentException"],
            ["BG-WB-05", "血糖评估", "5.0 + random/null", "非法测量时段异常路径", "抛出 IllegalArgumentException"],
            ["BG-WB-06", "血糖评估", "35.0 + 餐后", "最大合法值路径", "等级为高血糖，返回字段完整"],
        ],
        [1.8, 2.2, 4.2, 4.0, 3.8],
    )

    analysis_para = find_paragraph(doc, lambda text: text.startswith("正文字体为“宋体五号”"), 0)
    replace_paragraph(
        analysis_para,
        "运行 mvn test 后，项目共执行 149 个 JUnit 测试，失败 0 个，错误 0 个，跳过 0 个，构建结果为 BUILD SUCCESS。"
        "其中本人新增的 BpEvaluatorWhiteBoxTest 和 BgEvaluatorWhiteBoxTest 各包含 6 个测试方法，均运行通过。"
        "从结果看，血压评估模块能够正确处理正常、偏高、高血压三类路径，并能拦截非正数和超范围数据；"
        "血糖评估模块能够根据空腹和餐后两种测量时段选择不同标准，且 3.9、6.1、7.0、7.8、11.1、35.0 等边界点均符合预期。"
        "因此，本人负责的血压评估和血糖评估模块在当前测试范围内通过白盒测试。",
    )

    # Remove remaining obvious instruction paragraphs in the report body.
    for p in doc.paragraphs:
        text = p.text.strip()
        if text.startswith("正文字体为“宋体五号”") or text.startswith("注意说明文字需要删除") or text.startswith("如果包含图片"):
            clear_paragraph(p)

    # Keep sections outside this responsibility from showing template guidance.
    for p in doc.paragraphs:
        text = p.text.strip()
        if text.startswith("本部分的内容需要列出采用的测试方法及其对应的测试用例。可以手工测试"):
            replace_paragraph(p, "实验2黑盒测试用例由负责界面和手工测试的组员补充。")
        elif text in {"要列出每个人的心得与体会。例如，", "张三：***", "李四：****"}:
            clear_paragraph(p)

    doc.save(OUT_PATH)
    print(OUT_PATH)


if __name__ == "__main__":
    main()
