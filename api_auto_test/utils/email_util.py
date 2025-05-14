# -*- coding: utf-8 -*-
import sys
import os
# 将项目根目录添加到 sys.path 中
project_root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.insert(0, project_root)

import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart  # 混合MIME格式，支持上传附件
from email.header import Header  # 用于使用中文邮件主题
from config import Config
from utils.log_util import Log

mylogger = Log(__name__).getlogger()


def send_email(report_file):

    # 检查文件是否存在
    if not os.path.exists(report_file):
        absolute_path = os.path.abspath(report_file)
        mylogger.error(f"报告文件 {absolute_path} 不存在，请检查！")
        return

    msg = MIMEMultipart()  # 混合MIME格式
    try:
        # 读取报告文件内容并添加到邮件正文中
        with open(report_file, encoding="utf-8") as f:
            msg.attach(MIMEText(f.read(), "html", "utf-8"))  

        msg["From"] = Config.smtp_user  # 从配置文件中获取发件人
        recipients = Config.receiver.split(',') if ',' in Config.receiver else [Config.receiver]
        msg["To"] = ', '.join(recipients)  # 支持多个收件人
        msg["Subject"] = Header(Config.subject, "utf-8")  # 从配置文件中获取邮件主题

        # 添加附件
        with open(report_file, "rb") as f:
            att1 = MIMEText(f.read(), "base64", "utf-8")
        att1["Content-Type"] = "application/octet-stream"
        att1["Content-Disposition"] = f'attachment; filename="{report_file.split(os.sep)[-1]}"'
        msg.attach(att1)

        # 连接 SMTP 服务器并发送邮件
        with smtplib.SMTP_SSL(Config.smtp_server) as smtp:
            smtp.login(Config.smtp_user, Config.smtp_password)
            smtp.sendmail(Config.smtp_user, recipients, msg.as_string())
        mylogger.info("邮件发送完成！")
    except Exception as e:
        mylogger.error(f"邮件发送失败，错误信息: {str(e)}")
