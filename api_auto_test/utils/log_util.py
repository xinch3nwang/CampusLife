# -*- coding: utf-8 -*-

import logging
import os.path
import time
import logging.handlers
import subprocess
from config import Config


class Log(object):
    def __init__(self, loggerName=None, fileName="test.log"):
        """
        初始化日志记录器。

        :param loggerName: 日志记录器的名称，默认为 None
        :param fileName: 日志文件的名称，默认为 "test.log"
        """
        # 检查日志存储路径是否存在，如果不存在则创建该路径
        if not os.path.exists(Config.log_path):
            # 在 Windows 系统中，使用 mkdir 命令创建目录
            subprocess.run("mkdir {}".format(Config.log_path), shell=True)

        # 创建一个日志记录器对象
        self.logger = logging.getLogger(loggerName)
        # 设置日志记录器的日志级别为 DEBUG，即记录所有级别的日志
        self.logger.setLevel(logging.DEBUG)

        # 获取当前日期，格式为 "YYYYMMDD"
        timenow = time.strftime("%Y%m%d", time.localtime(time.time()))
        # 拼接完整的日志文件路径
        self.log_name = Config.log_path + fileName

        # 创建一个文件处理器，用于将所有日志写入文件，以追加模式打开文件，并指定编码为 UTF-8
        all_fh = logging.FileHandler(self.log_name, mode="a+", encoding="utf-8")
        # 设置文件处理器的日志级别为 INFO，即只记录 INFO 及以上级别的日志
        all_fh.setLevel(logging.INFO)

        # 创建一个流处理器，用于将日志输出到控制台
        ch = logging.StreamHandler()
        # 设置流处理器的日志级别为 INFO，即只记录 INFO 及以上级别的日志
        ch.setLevel(logging.INFO)

        # 定义日志的输出格式，包含时间、日志级别、行号、文件名和日志信息
        all_formatter = logging.Formatter(
            "[%(asctime)s] [%(levelname)s] line:%(lineno)d %(filename)s  %(message)s"
        )
        # 将日志格式应用到文件处理器
        all_fh.setFormatter(all_formatter)
        # 将日志格式应用到流处理器
        ch.setFormatter(all_formatter)

        # 将文件处理器添加到日志记录器中
        self.logger.addHandler(all_fh)
        # 将流处理器添加到日志记录器中
        self.logger.addHandler(ch)

        # 关闭文件处理器，释放资源
        all_fh.close()
        # 关闭流处理器，释放资源
        ch.close()

    def __str__(self):
        """
        返回日志记录器和日志文件名的字符串表示。

        :return: 包含日志记录器和日志文件名的字符串
        """
        return "logger为: %s, 日志文件名为: %s" % (
            self.logger,
            self.log_name,
        )

    def getlogger(self):
        return self.logger


if __name__ == "__main__":
    # 主程序入口，此处暂不执行任何操作
    pass
