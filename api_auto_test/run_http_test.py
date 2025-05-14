# -*- coding: utf-8 -*-
import os
import datetime
import time
from httpcase import test_interface
from utils.html_report_util import create_html
from utils.log_util import Log
from utils.excel_util import ExcelReader
from config import Config
from utils.email_util import send_email
import subprocess

# 初始化日志记录器
mylogger = Log(__name__).getlogger()


"""执行测试的主要文件"""
def test_create(casefilename, isSendMail=False, nums=None):
    """
    执行 HTTP 接口测试并生成测试报告。

    :param casefilename: 测试用例 Excel 文件的文件名
    """
    # 记录测试开始时间
    starttime = datetime.datetime.now()
    # 生成当前时间的时间戳，格式为 "YYYYMMDDHHMM"
    day = time.strftime("%Y%m%d%H%M", time.localtime(time.time()))
    # 获取配置文件中定义的测试用例路径
    case_path = Config.case_path
    # 检查测试用例路径是否存在，如果不存在则创建该路径
    if not os.path.exists(case_path):
        # 在 Windows 系统中，使用 mkdir 命令创建目录
        subprocess.run("mkdir {}".format(case_path), shell=True)
    # 拼接完整的测试用例文件路径
    testcase_file = case_path + casefilename
    # 从 Excel 文件中逐行读取所有测试用例数据
    data_list = ExcelReader(
        testcase_file, Config.sheet_name
    ).read_all_data_line_by_line()
    # 用于存储测试用例的 ID
    list_id = []
    # 用于存储测试用例的名称
    list_name = []
    # 用于存储测试用例的请求参数
    list_params = []
    # 用于存储测试用例的请求 URL
    list_url = []
    # 用于存储测试用例的请求类型
    list_type = []
    # 用于存储测试用例的预期结果
    list_expect = []
    # 用于存储测试用例的前置用例 ID
    list_pre_caseid = []
    # 用于存储测试用例的期待执行结果
    list_expect = []
    # 获取测试用例的数量
    size = len(data_list) if nums is None else nums
    # 遍历所有测试用例数据
    for i in range(size):
        # 获取当前测试用例的数据
        data = data_list[i]
        # 获取当前测试用例的请求参数
        params = data.get("请求数据")
        # 获取当前测试用例的请求 URL
        url = data.get("url")
        # 获取当前测试用例的请求类型
        request_type = data.get("请求方式")
        # 获取当前测试用例的预期结果
        expect_result = data.get("期望值")
        # 获取前置用例ID
        pre_caseid = data.get("前置用例ID")
        # 获取当前测试用例的 ID
        id = data.get("用例ID")
        # 获取当前测试用例的名称
        name = data.get("用例名")
        # 将当前测试用例的 ID 添加到列表中
        list_id.append(id)
        # 将当前测试用例的前置用例 ID 添加到列表中
        list_pre_caseid.append(pre_caseid)
        # 将当前测试用例的名称添加到列表中
        list_name.append(name)
        # 将当前测试用例的请求参数添加到列表中
        list_params.append(params)
        # 将当前测试用例的请求 URL 添加到列表中
        list_url.append(url)
        # 将当前测试用例的请求类型添加到列表中
        list_type.append(request_type)
        # 将当前测试用例的预期结果添加到列表中
        list_expect.append(expect_result)

    # 调用测试接口函数执行测试，并获取测试结果
    list_result, fail_num, pass_num, list_json = test_interface(casefilename)
    # 拼接完整的 HTML 报告文件路径
    filepath = Config.html_path + "{}_result.html".format(day)

    # 检查 HTML 报告文件是否存在，如果不存在则创建该文件
    if not os.path.exists(filepath):
        # 在 Windows 系统中，使用 type nul 命令创建文件
        subprocess.run("type nul > {}".format(filepath), shell=True)
    # 记录测试结束时间
    endtime = datetime.datetime.now()

    # 准备测试用例数据
    test_cases = []
    for i in range(len(list_id)):
        test_cases.append({
            'id': list_id[i],
            'name': list_name[i],
            'content': list_params[i],
            'url': list_url[i],
            'method': list_type[i],
            'yuqi': list_expect[i],
            'json': list_json[i],
            'relust': list_result[i],
            'result_class': 'success' if list_result[i] == 'pass' else 'danger'
        })

    # 调用新的create_html函数
    file = create_html(
        filepath=filepath,
        title="http接口自动化测试报告",
        starttime=starttime,
        endtime=endtime,
        pass_num=pass_num,
        fail_num=fail_num,
        test_cases=test_cases
    )

    if isSendMail:
        send_email(file)


if __name__ == "__main__":
    # 当脚本作为主程序运行时，执行测试并生成报告
    test_create("httpcase.xlsx", isSendMail=False)
