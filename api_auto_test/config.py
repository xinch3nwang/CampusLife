# -*- coding: utf-8 -*-
import os


class Config(object):
    """
    基础配置类，用于存储项目的各种配置信息，包括路径、日志、报告和邮件配置等。
    """

    # 项目路径
    prj_path = os.path.dirname(
        os.path.abspath(__file__)
    )  # 当前文件的绝对路径的上一级，__file__指当前文件

    # 数据目录，暂时设置为项目目录
    data_path = prj_path  
    # 用例目录，暂时设置为项目目录
    test_path = prj_path  

    # 日志文件路径，将日志文件放在项目目录下，命名为 log.txt
    log_file = os.path.join(prj_path, "log.txt")  # 也可以每天生成新的日志文件
    # 报告文件路径，将报告文件放在项目目录下，命名为 report.html
    report_file = os.path.join(prj_path, "report.html")  # 也可以每次生成新的报告

    # 测试环境配置部分
    # 测试基础URL
    test_base_url = "http://127.0.0.1:8000"  # 根据实际API地址修改
    # excel 测试用例数据路径，相对于项目目录的路径
    case_path = "./test_cases/"  
    # excel 文件的 sheet 页名字
    sheet_name = "httpcase"  
    # html 结果报告路径，相对于项目目录的路径
    html_path = "./test_reports/"  
    # 日志文件存储路径，相对于项目目录的路径
    log_path = "./log/"

    # 邮件配置部分
    # SMTP 服务器地址
    smtp_server = "smtp.sina.com"
    # 发件人邮箱账号
    smtp_user = "xinch3nwang@sina.com"
    # 发件人邮箱密码
    smtp_password = "62827f94d76a16af"

    # 发件人邮箱，与 SMTP 用户账号相同
    sender = smtp_user  
    # 收件人邮箱
    receiver = "xinchen0709@foxmail.com"  
    # 邮件主题
    subject = "接口测试报告"

    # JWT配置
    # JWT token过期时间（秒）
    jwt_expire = 3600
    # JWT刷新时间（秒）
    jwt_refresh = 3000
    # JWT密钥
    jwt_secret = "mysecretkey"
