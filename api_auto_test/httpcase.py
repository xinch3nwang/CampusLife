# -*- coding: utf-8 -*-

from utils.myexcel import OperateExcel
from requests_client import RequestUtil
from config import Config
import os
import subprocess
from utils.mylog import Log
import json
import jwt
import time

mylogger = Log(__name__).getlogger()

# 存储JWT token
jwt_token = None
refresh_token = None

# 检查token是否过期
def is_token_expired(token):
    try:
        decoded = jwt.decode(token, Config.jwt_secret, algorithms=["HS256"])
        return False
    except jwt.ExpiredSignatureError:
        return True


def test_interface(case_filename):
    """
    测试接口的主函数，从 Excel 文件中读取测试用例并执行请求，根据预期结果判断测试是否通过。

    :param case_filename: 测试用例 Excel 文件的文件名
    :return: 包含测试结果的列表，失败用例数量，成功用例数量，以及所有请求的响应结果列表
    """
    # 获取配置文件中定义的测试用例路径
    case_path = Config.case_path
    # 检查测试用例路径是否存在，如果不存在则创建该路径
    if not os.path.exists(case_path):
        subprocess.run("mkdir -p {}".format(case_path), shell=True)
    # 拼接完整的测试用例文件路径
    testcase_file = case_path + case_filename
    # 从 Excel 文件中逐行读取所有测试用例数据
    data_list = OperateExcel(
        testcase_file, Config.sheet_name
    ).read_all_data_line_by_line()

    # 初始化成功用例数量
    pass_num = 0
    # 初始化失败用例数量
    fail_num = 0
    # 用于存储请求的响应结果
    list_json = []
    # 用于存储每个测试用例的执行结果（pass 或 fail）
    list_result = []

    # 遍历所有测试用例数据
    for i in range(len(data_list)):
        # 获取当前测试用例的数据
        request_data = data_list[i]
        # 获取请求的数据类型
        data_type = request_data.get("数据类型")
        # 获取测试用例的 ID
        case_id = request_data.get("用例ID")
        # 获取请求的数据，并去除其中的换行符
        params = request_data.get("请求数据")
        if params is not None:
            params = params.replace("\n", "")
        else:
            mylogger.error(f"用例 ID {case_id} 的请求数据为空，跳过该用例")
            continue  # 可根据实际情况选择跳过该用例或者进行其他处理
        # 获取请求的 URL
        url = Config.test_base_url + request_data.get("URL")
        # 获取请求的方式（POST 或 GET）
        request_type = request_data.get("请求方式")
        # 获取预期结果
        expect_result = request_data.get("期望值")
        # 从预期结果中提取预期的状态码
        expect_code = str(expect_result.split("=")[1])
        # 创建请求工具实例，传入数据类型
        myrequest = RequestUtil(str(data_type))
        # 如果请求方式为 POST，则发送 POST 请求
        # 检查并更新token
        global jwt_token
        if not jwt_token or is_token_expired(jwt_token):
            # 先获取新的token
            refresh_url = Config.test_base_url + "/account/token/refresh/"
            refresh_headers = {"Authorization": f"Bearer {refresh_token}"} if refresh_token else {}
            refresh_response = myrequest.post(url=refresh_url, headers=refresh_headers)
            if isinstance(refresh_response, dict) and "access_token" in refresh_response:
                jwt_token = refresh_response["access_token"]
                if "refresh_token" in refresh_response:
                    refresh_token = refresh_response["refresh_token"]
        
        # 添加token到请求头
        headers = {"Authorization": f"Bearer {jwt_token}"} if jwt_token else {}
        
        if request_type == "POST":
            # print(url)
            # print(params)
            result = myrequest.post(url=url, params=json.loads(params), headers=headers)  # 返回值是dict
        # 如果请求方式为 GET，则发送 GET 请求
        if request_type == "GET":
            # print({"url": url, "params": json.loads(params)})
            result = myrequest.get(url=url, params=json.loads(params), headers=headers)

        if isinstance(result, dict) and "result" in result and isinstance(result["result"], dict):
            real_result = result.get("result").get("code")
            # 如果存在access_token和refresh_token则更新全局变量
            if result.get("access_token"):
                global jwt_token
                jwt_token = result["access_token"]
            if result.get("refresh_token"):
                global refresh_token
                refresh_token = result["refresh_token"]
        else:
            mylogger.error("无法从结果中获取状态码，结果: {}".format(result))
            real_result = None

        if real_result is not None and expect_code == str(real_result):
            mylogger.info("成功，case id {}".format(case_id))
            # 将响应结果添加到列表中
            list_json.append(result)
            # 记录测试结果为 pass
            list_result.append("pass")
            # 成功用例数量加 1
            pass_num += 1
        else:
            # 记录失败日志，包含详细的错误信息
            mylogger.error(
                "失败, case id {}, 请求参数:{}, url: {}, 数据类型:{}, 请求类型:{}, 错误结果:{}".format(
                    case_id, params, url, data_type, request_type, result
                )
            )
            # 失败用例数量加 1
            fail_num += 1
            # 记录测试结果为 fail
            list_result.append("fail")
            # 将响应结果添加到列表中
            list_json.append(result)

    return list_result, fail_num, pass_num, list_json


if __name__ == "__main__":
    test_interface("httpcase.xlsx")
