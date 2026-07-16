from pathlib import Path

from docx import Document
from docx.enum.section import WD_SECTION
from docx.enum.table import WD_ALIGN_VERTICAL
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml import OxmlElement
from docx.oxml.ns import qn
from docx.shared import Cm, Pt, RGBColor


ROOT = Path(__file__).resolve().parents[1]
OUT_DIR = ROOT / "docs"
OUT_PATH = OUT_DIR / "blood-pressure-blood-glucose-white-box-report.docx"


def set_run_font(run, size=10.5, bold=False, color=None):
    run.font.name = "Times New Roman"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "宋体")
    run._element.rPr.rFonts.set(qn("w:ascii"), "Times New Roman")
    run._element.rPr.rFonts.set(qn("w:hAnsi"), "Times New Roman")
    run.font.size = Pt(size)
    run.bold = bold
    if color:
        run.font.color.rgb = RGBColor.from_string(color)


def set_paragraph_format(paragraph, first_line=False):
    paragraph.paragraph_format.line_spacing = 1.25
    paragraph.paragraph_format.space_before = Pt(0)
    paragraph.paragraph_format.space_after = Pt(6)
    if first_line:
        paragraph.paragraph_format.first_line_indent = Cm(0.74)


def add_paragraph(doc, text="", first_line=True):
    p = doc.add_paragraph()
    set_paragraph_format(p, first_line=first_line)
    run = p.add_run(text)
    set_run_font(run)
    return p


def add_heading(doc, text, level=1):
    p = doc.add_paragraph()
    set_paragraph_format(p, first_line=False)
    if level == 1:
        p.paragraph_format.space_before = Pt(12)
        p.paragraph_format.space_after = Pt(8)
        size = 15
    else:
        p.paragraph_format.space_before = Pt(8)
        p.paragraph_format.space_after = Pt(6)
        size = 12
    run = p.add_run(text)
    set_run_font(run, size=size, bold=True, color="1F4D78")
    return p


def set_cell_text(cell, text, bold=False, align=WD_ALIGN_PARAGRAPH.LEFT):
    cell.vertical_alignment = WD_ALIGN_VERTICAL.CENTER
    cell.text = ""
    p = cell.paragraphs[0]
    p.alignment = align
    set_paragraph_format(p, first_line=False)
    p.paragraph_format.space_after = Pt(0)
    run = p.add_run(text)
    set_run_font(run, size=9.5, bold=bold)


def set_table_borders(table):
    tbl = table._tbl
    tbl_pr = tbl.tblPr
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
        element.set(qn("w:color"), "B7C4D6")


def shade_cell(cell, fill):
    tc_pr = cell._tc.get_or_add_tcPr()
    shd = tc_pr.find(qn("w:shd"))
    if shd is None:
        shd = OxmlElement("w:shd")
        tc_pr.append(shd)
    shd.set(qn("w:fill"), fill)


def add_table(doc, headers, rows, widths_cm):
    table = doc.add_table(rows=1, cols=len(headers))
    table.autofit = False
    set_table_borders(table)
    for i, header in enumerate(headers):
        cell = table.rows[0].cells[i]
        cell.width = Cm(widths_cm[i])
        shade_cell(cell, "E8EEF5")
        set_cell_text(cell, header, bold=True, align=WD_ALIGN_PARAGRAPH.CENTER)
    for row in rows:
        cells = table.add_row().cells
        for i, text in enumerate(row):
            cells[i].width = Cm(widths_cm[i])
            set_cell_text(cells[i], str(text))
    doc.add_paragraph()
    return table


def main():
    OUT_DIR.mkdir(exist_ok=True)
    doc = Document()
    section = doc.sections[0]
    section.top_margin = Cm(2.54)
    section.bottom_margin = Cm(2.54)
    section.left_margin = Cm(2.54)
    section.right_margin = Cm(2.54)

    styles = doc.styles
    normal = styles["Normal"]
    normal.font.name = "Times New Roman"
    normal._element.rPr.rFonts.set(qn("w:eastAsia"), "宋体")
    normal.font.size = Pt(10.5)

    title = doc.add_paragraph()
    title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    set_paragraph_format(title, first_line=False)
    title.paragraph_format.space_after = Pt(12)
    run = title.add_run("血压评估与血糖评估白盒测试报告部分")
    set_run_font(run, size=16, bold=True, color="0B2545")

    add_heading(doc, "2.1 实验1测试计划（本人负责模块）", 1)
    add_heading(doc, "（1）系统功能概述", 2)
    add_paragraph(
        doc,
        "本项目为老年人健康管理助手，采用 Java 17、JavaFX 和 Maven 构建。本人负责血压评估与血糖评估两个业务逻辑模块。"
        "血压评估模块接收收缩压和舒张压，输出正常、偏高或高血压等级及健康建议；血糖评估模块接收血糖值和测量时段，"
        "根据空腹或餐后标准输出低血糖、正常、偏高或高血糖等级及健康建议。",
    )
    add_heading(doc, "（2）测试目标", 2)
    add_paragraph(
        doc,
        "本次白盒测试目标是验证 BpEvaluator.evaluate() 与 BgEvaluator.evaluate() 的内部判断逻辑、异常处理逻辑和结果封装逻辑是否正确。"
        "测试重点包括复合条件左右操作数、主要分支、边界值、异常路径以及返回 Map 字段完整性。",
    )
    add_heading(doc, "（3）测试范围", 2)
    add_table(
        doc,
        ["测试对象", "主要方法", "覆盖内容"],
        [
            ["BpEvaluator", "evaluate(double systolic, double diastolic)", "血压值合法性校验、正常/偏高/高血压分支、返回字段"],
            ["BgEvaluator", "evaluate(double value, String period)", "血糖值合法性校验、测量时段校验、空腹/餐后区间选择、四类血糖结果分支"],
        ],
        [3.2, 5.8, 7.0],
    )
    add_heading(doc, "（4）测试环境与工具", 2)
    add_table(
        doc,
        ["项目", "内容"],
        [
            ["操作系统", "Windows"],
            ["开发语言", "Java 17"],
            ["构建工具", "Maven"],
            ["测试框架", "JUnit 5"],
            ["执行命令", "mvn test"],
            ["测试文件", "BpEvaluatorWhiteBoxTest.java、BgEvaluatorWhiteBoxTest.java"],
        ],
        [4.0, 12.0],
    )
    add_heading(doc, "（5）白盒测试方法", 2)
    add_table(
        doc,
        ["方法", "在本模块中的应用"],
        [
            ["语句覆盖", "执行正常返回、异常抛出、等级赋值和结果 Map 封装语句。"],
            ["分支覆盖/判定覆盖", "覆盖血压正常、偏高、高血压及血糖低血糖、正常、偏高、高血糖等 if/else 分支。"],
            ["条件覆盖", "分别验证 systolic、diastolic、period 等复合条件中各子条件为真和为假的情况。"],
            ["路径覆盖", "覆盖非法输入路径、正常评估路径、边界进入下一等级路径和最大合法值路径。"],
            ["边界值测试", "围绕 120/80、140/90、3.9、6.1、7.0、7.8、11.1、35 等判断边界设计用例。"],
        ],
        [3.2, 12.8],
    )

    add_heading(doc, "3.1 实验1测试用例及结果分析（本人负责模块）", 1)
    add_heading(doc, "（1）测试用例", 2)
    add_table(
        doc,
        ["编号", "测试对象", "输入数据", "覆盖目标", "预期结果"],
        [
            ["BP-WB-01", "血压评估", "119.9 / 79.9", "正常路径、严格上界", "等级为正常，返回输入值和建议"],
            ["BP-WB-02", "血压评估", "120 / 79、119 / 80、139.9 / 89.9", "偏高路径、条件覆盖", "等级为偏高"],
            ["BP-WB-03", "血压评估", "140 / 89、139 / 90、160 / 100", "高血压路径、条件覆盖", "等级为高血压"],
            ["BP-WB-04", "血压评估", "0 / 80、120 / 0、-1 / -1", "非正数异常路径", "抛出 IllegalArgumentException"],
            ["BP-WB-05", "血压评估", "301 / 80、120 / 201、301 / 201", "超范围异常路径", "抛出 IllegalArgumentException"],
            ["BP-WB-06", "血压评估", "300 / 200", "最大合法值路径", "等级为高血压，返回字段完整"],
            ["BG-WB-01", "血糖评估", "空腹：3.8、3.9、6.1、6.1001、7.0、7.0001", "空腹区间选择、边界值", "依次覆盖低血糖、正常、偏高、高血糖"],
            ["BG-WB-02", "血糖评估", "餐后：3.8、3.9、7.8、7.8001、11.1、11.1001", "餐后区间选择、边界值", "依次覆盖低血糖、正常、偏高、高血糖"],
            ["BG-WB-03", "血糖评估", "6.2 + 空腹/餐后", "同值不同时段路径", "空腹为偏高，餐后为正常"],
            ["BG-WB-04", "血糖评估", "0、-0.1、35.1", "非法血糖值异常路径", "抛出 IllegalArgumentException"],
            ["BG-WB-05", "血糖评估", "5.0 + random/null", "非法测量时段异常路径", "抛出 IllegalArgumentException"],
            ["BG-WB-06", "血糖评估", "35.0 + 餐后", "最大合法值路径", "等级为高血糖，返回字段完整"],
        ],
        [1.8, 2.4, 4.5, 4.0, 3.5],
    )

    add_heading(doc, "（2）运行结果及分析", 2)
    add_paragraph(
        doc,
        "新增白盒测试文件为 BpEvaluatorWhiteBoxTest.java 和 BgEvaluatorWhiteBoxTest.java，分别包含 6 个测试方法。"
        "运行 mvn test 后，项目共执行 149 个 JUnit 测试，失败 0 个，错误 0 个，跳过 0 个，构建结果为 BUILD SUCCESS。",
    )
    add_paragraph(
        doc,
        "从运行结果看，血压评估模块能够正确处理正常、偏高、高血压三类路径，并能对非正数和超出合理范围的数据抛出异常。"
        "测试中特别验证了 120/80、140/90 相关边界，说明代码中使用的 '<' 与 '>=' 分类逻辑符合当前实现预期。",
    )
    add_paragraph(
        doc,
        "血糖评估模块能够根据空腹和餐后两种测量时段选择不同区间标准。测试中同样的血糖值 6.2 在空腹时判定为偏高、餐后时判定为正常，"
        "说明测量时段分支生效。围绕 3.9、6.1、7.0、7.8、11.1 等边界的测试均通过，非法测量时段和非法血糖值也能被正确拦截。",
    )
    add_paragraph(
        doc,
        "本次白盒测试覆盖了本人负责模块的主要语句、分支、条件和可达路径。后续黑盒测试可从用户界面角度继续验证输入框校验、错误提示展示和实际操作流程。",
    )

    doc.save(OUT_PATH)
    print(OUT_PATH)


if __name__ == "__main__":
    main()
