# -*- coding: utf-8 -*-

import requests
import json
from utils.log_util import Log
from retrying import retry

mylogger = Log(__name__).getlogger()


class RequestUtil:
    def __init__(self, data_type):
        """
        初始化请求工具类，根据数据类型设置请求头。

        :param data_type: 请求数据的类型，支持 'xml' 和 'json'
        """
        # xml格式
        if data_type == "xml" or data_type == "XML":
            self.data_type = data_type
            self.headers = {
                "Content-Type": "text/xml",
                # "User-Agent": "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E; rv:11.0) like Gecko"
            }
        # json格式
        if data_type == "json" or data_type == "JSON":
            self.data_type = data_type
            self.headers = {
                "Content-Type": "application/json",
                # "User-Agent": "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E; rv:11.0) like Gecko"
            }



    @retry(stop_max_attempt_number=3, wait_random_min=1, wait_random_max=5)
    def get(self, url, params):  # get请求
        """
        发送 GET 请求，并尝试将响应内容解析为 JSON 格式。
        若请求失败或解析出错，会进行最多 3 次重试。

        :param url: 请求的 URL
        :param params: 请求参数
        :return: 包含请求状态码和响应结果的字典
        """
        try:
            # 发送 GET 请求
            r = requests.get(url, params=params)#, headers=self.headers)
            # 设置响应编码为 UTF-8
            r.encoding = "UTF-8"
            # 解析 JSON 格式
            json_response = json.loads(r.text)
            return {"get_code": 0, "result": json_response}
        except Exception as e:
            # 记录 GET 请求出错信息
            mylogger.error("get请求出错，出错原因: {}".format(e), exc_info=True)
            return {"get_code": 1, "result": "get请求出错，出错原因:%s" % e}


    @retry(stop_max_attempt_number=3, wait_random_min=1, wait_random_max=5)
    def post(self, url, params):  # post请求
        """
        发送 POST 请求，并尝试将响应内容解析为 JSON 格式。
        若请求失败或解析出错，会进行最多 3 次重试。

        :param url: 请求的 URL
        :param params: 请求参数
        :return: 包含请求状态码和响应结果的字典
        """ 
        try:
            data = {
                'username': 'testuser',
                'password': 'Test@1234'
            }
            # r = requests.post(url, data=data)
            # 发送 POST 请求
            r = requests.post(url, data=params)#, headers=self.headers)
            # 解析 JSON 格式
            json_response = json.loads(r.text)
            # print(json_response)
            return {"post_code": 0, "result": json_response}
        except Exception as e:
            # 记录 POST 请求出错信息
            mylogger.error("post请求出错，出错原因: {}".format(e), exc_info=True)
            return {"post_code": 1, "result": "post请求出错，出错原因:%s" % e}


    @retry(stop_max_attempt_number=3, wait_random_min=1, wait_random_max=5)
    def put(self, url, params):  # put请求
        """
        发送 PUT 请求，并尝试将响应内容解析为 JSON 格式。
        若请求失败或解析出错，会进行最多 3 次重试。

        :param url: 请求的 URL
        :param params: 请求参数
        :return: 包含请求状态码和响应结果的字典
        """
        try:
            # 发送 PUT 请求
            me = requests.put(url, params)
            # 解析 JSON 格式
            json_response = json.loads(me.text)
            return {"code": 0, "result": json_response}
        except Exception as e:
            # 记录 PUT 请求出错信息
            mylogger.error("put请求出错，出错原因: {}".format(e), exc_info=True)

            return {"code": 1, "result": "put请求出错，出错原因:%s" % e}


    @retry(stop_max_attempt_number=3, wait_random_min=1, wait_random_max=5)
    def delete(self, url, params):  # delete请求
        """
        发送 DELETE 请求，并尝试将响应内容解析为 JSON 格式。
        若请求失败或解析出错，会进行最多 3 次重试。

        :param url: 请求的 URL
        :param params: 请求参数
        :return: 包含请求状态码和响应结果的字典
        """
        try:
            # 发送 DELETE 请求
            r = requests.delete(url, params=params)
            # 解析 JSON 格式
            json_response = json.loads(r.text)
            return {"delete_code": 0, "result": json_response}
        except Exception as e:
            # 记录 DELETE 请求出错信息
            mylogger.error("delete请求出错，出错原因: {}".format(e), exc_info=True)
            return {"delete_code": 1, "result": "delete请求出错，出错原因:%s" % e}    


if __name__ == "__main__":
    pass
