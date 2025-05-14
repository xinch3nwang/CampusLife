#  -*- coding: utf-8 -*-
from jinja2 import Environment, FileSystemLoader
import os


def create_html(filepath, title, starttime, endtime, pass_num, fail_num, test_cases):
    """
    使用Jinja2模板生成HTML测试报告
    
    :param filepath: 保存HTML文件的路径
    :param title: 页面标题
    :param starttime: 测试开始时间
    :param endtime: 测试结束时间
    :param pass_num: 通过的测试用例数量
    :param fail_num: 失败的测试用例数量
    :param test_cases: 测试用例列表，每个用例是包含以下字段的字典:
        - id: 用例ID
        - name: 用例名称
        - content: 请求内容
        - url: 请求URL
        - method: 请求方式
        - yuqi: 预期结果
        - json: 实际返回JSON
        - relust: 测试结果('pass'或'fail')
        - result_class: 结果类别('success'或'danger')
    :return: 保存的HTML文件路径
    """
    # 设置模板环境
    env = Environment(loader=FileSystemLoader(os.path.join(os.path.dirname(__file__), 'templates')))
    template = env.get_template('template.html')
    
    # 渲染模板
    html_content = template.render(
        title=title,
        starttime=starttime,
        endtime=endtime,
        duration=endtime - starttime,
        pass_num=pass_num,
        fail_num=fail_num,
        test_cases=test_cases
    )
    
    # 写入文件
    with open(filepath, 'wb') as f:
        f.write(html_content.encode('utf-8'))
    
    return filepath

# # 定义 HTML 页面的标题
# titles = '接口测试'

# def title(titles):
#     """
#     生成 HTML 页面的头部部分，包含元数据、标题和样式。

#     :param titles: 页面的标题文本
#     :return: 包含 HTML 头部的字符串
#     """
#     title = '''<!DOCTYPE html>
# <html>
# <head>
	
# 	<meta http-equiv=Content-Type content="text/html; charset=utf-8">
# 	<title>%s</title>
# 	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
# 	<meta name="viewport" content="width=device-width, initial-scale=1.0">
#     <!-- 引入 Bootstrap -->
#     <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
#     <!-- HTML5 Shim 和 Respond.js 用于让 IE8 支持 HTML5元素和媒体查询 -->
#     <!-- 注意： 如果通过 file://  引入 Respond.js 文件，则该文件无法起效果 -->
#     <!--[if lt IE 9]>
#      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
#      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
#     <![endif]-->
#     <style type="text/css">
#         .hidden-detail,.hidden-tr{
#             display:none;
#         }
#     </style>
# </head>
# <body>
# 	''' % (titles)
	
#     return title

# # 定义 HTML 页面的主体部分起始内容
# connent = '''
# <div  class='col-md-4 col-md-offset-4' style='margin-left:3%;'>
# <h1>接口测试的结果</h1>'''

# def shouye(starttime, endtime, pass_num, fail_num):
#     """
#     生成接口测试结果的摘要表格，包含开始时间、结束时间、耗时和测试结果统计。

#     :param starttime: 测试开始时间
#     :param endtime: 测试结束时间
#     :param pass_num: 通过的测试用例数量
#     :param fail_num: 失败的测试用例数量
#     :return: 包含摘要表格的 HTML 字符串
#     """
#     summary = '''
#     <table  class="table table-hover table-condensed">
#             <tbody>
#                 <tr>
# 		<td><strong>开始时间:</strong> %s</td>
# 		</tr>
# 		<td><strong>结束时间:</strong> %s</td></tr>
# 		<td><strong>耗时:</strong> %s</td></tr>
# 		<td><strong>结果:</strong>
# 			<span >Pass: <strong >%s</strong>
# 			Fail: <strong >%s</strong>
			 
# 			    </span></td>                  
# 			   </tr> 
# 			   </tbody></table>
# 			   </div> ''' % (starttime, endtime, (endtime - starttime), pass_num, fail_num)
#     return summary

# # 定义 HTML 页面中测试用例详情部分的起始内容
# detail = '''<div class="row " style="margin:60px">
#         <div style='    margin-top: 18%;' >
#         <div class="btn-group" role="group" aria-label="...">
#             <button type="button" id="check-all" class="btn btn-primary">所有用例</button>
#             <button type="button" id="check-success" class="btn btn-success">成功用例</button>
#             <button type="button" id="check-danger" class="btn btn-danger">失败用例</button>
#         </div>
#         <div class="btn-group" role="group" aria-label="...">
#         </div>
#         <table class="table table-hover table-condensed table-bordered" style="word-wrap:break-word; word-break:break-all;  margin-top: 7px;">
# 		<tr >
#             <td ><strong>用例ID&nbsp;</strong></td>
#             <td><strong>用例名字</strong></td>
#             <td><strong>请求内容</strong></td>
#             <td><strong>url</strong></td>
#             <td><strong>请求方式</strong></td>
#             <td><strong>预期</strong></td>
#             <td><strong>实际返回</strong></td>  
#             <td><strong>结果</strong></td>
#         </tr>
#     '''

# def passfail(tend):
#     """
#     根据测试结果生成对应的 HTML 表格单元格，通过用例显示绿色，失败用例显示红色。

#     :param tend: 测试结果，'pass' 表示通过，其他值表示失败
#     :return: 包含结果单元格的 HTML 字符串
#     """
#     if tend == 'pass':
#         htl = '''<td bgcolor="green">pass</td>'''
#     else:
#         htl = '''<td bgcolor="red">fail</td>'''
#     return htl

# def details(result, id, name, content, url, method, yuqi, json, relust):
#     """
#     生成单个测试用例的详情行，包含用例 ID、名称、请求内容等信息。

#     :param result: 测试结果的类别，'success' 或 'danger'
#     :param id: 用例 ID
#     :param name: 用例名称
#     :param content: 请求内容
#     :param url: 请求的 URL
#     :param method: 请求方式
#     :param yuqi: 预期结果
#     :param json: 实际返回的 JSON 数据
#     :param relust: 测试结果，'pass' 或 'fail'
#     :return: 包含测试用例详情行的 HTML 字符串
#     """
#     xiangqing = '''
#         <tr class="case-tr %s">
#             <td>%s</td>
#             <td>%s</td>
#             <td>%s</td>
#             <td>%s</td>
#             <td>%s</td>
#             <td>%s</td>
#             <td>%s</td>
#             %s
#         </tr>
#     ''' % (result, id, name, content, url, method, yuqi, json, passfail(relust))
#     return xiangqing

# # 定义 HTML 页面的尾部部分，包含脚本文件引入和交互逻辑
# weibu = '''</div></div></table><script src="https://code.jquery.com/jquery.js"></script>
# <script src="https://cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
# <script type="text/javascript">
# 	$("#check-danger").click(function(e){
# 	    $(".case-tr").removeClass("hidden-tr");
#         $(".success").addClass("hidden-tr"); 
# 	});

# 	$("#check-success").click(function(e){
# 		 $(".case-tr").removeClass("hidden-tr");
#          $(".danger").addClass("hidden-tr"); 
# 	});

# 	$("#check-all").click(function(e){
# 	    $(".case-tr").removeClass("hidden-tr");
# 	});
# </script>
# </body></html>'''

# def generate(titles, starttime, endtime, pass_num, fail_num, id, name, content, url, method, yuqi, json, result):
#     """
#     生成完整的 HTML 测试报告内容。

#     :param titles: 页面标题
#     :param starttime: 测试开始时间
#     :param endtime: 测试结束时间
#     :param pass_num: 通过的测试用例数量
#     :param fail_num: 失败的测试用例数量
#     :param id: 用例 ID 列表或单个 ID
#     :param name: 用例名称列表或单个名称
#     :param content: 请求内容列表或单个内容
#     :param url: 请求 URL 列表或单个 URL
#     :param method: 请求方式列表或单个方式
#     :param yuqi: 预期结果列表或单个结果
#     :param json: 实际返回的 JSON 数据列表或单个数据
#     :param result: 测试结果列表或单个结果
#     :return: 包含完整 HTML 测试报告的字符串
#     """
#     if type(name) == list:
#         relus = ' '
#         for i in range(len(name)):
#             if result[i] == "pass":
#                 clazz = "success"
#             else:
#                 clazz = 'danger'
#             relus += (details(clazz, id[i], name[i], content[i], url[i], method[i], yuqi[i], json[i], result[i]))

#         text = title(titles) + connent + shouye(starttime, endtime, pass_num, fail_num) + detail + relus + weibu
#     else:
#         text = title(titles) + connent + shouye(starttime, endtime, pass_num, fail_num) + detail + details(result, id,
#                                                                                                             name,
#                                                                                                             content, url,
#                                                                                                             method, yuqi,
#                                                                                                             json,
#                                                                                                             result) + weibu
#     return text

# def create_html(filepath, titles, starttime, endtime, pass_num, fail_num, id, name, content, url, method, yuqi, json,
#                results):
#     """
#     生成 HTML 测试报告并保存到指定文件。

#     :param filepath: 保存 HTML 文件的路径
#     :param titles: 页面标题
#     :param starttime: 测试开始时间
#     :param endtime: 测试结束时间
#     :param pass_num: 通过的测试用例数量
#     :param fail_num: 失败的测试用例数量
#     :param id: 用例 ID 列表或单个 ID
#     :param name: 用例名称列表或单个名称
#     :param content: 请求内容列表或单个内容
#     :param url: 请求 URL 列表或单个 URL
#     :param method: 请求方式列表或单个方式
#     :param yuqi: 预期结果列表或单个结果
#     :param json: 实际返回的 JSON 数据列表或单个数据
#     :param results: 测试结果列表或单个结果

#     :return: 保存的 HTML 文件路径
#     """
#     texts = generate(titles, starttime, endtime, pass_num, fail_num, id, name, content, url, method, yuqi, json,
#                      results)
#     with open(filepath, 'wb') as f:
#         f.write(texts.encode('utf-8'))
    
#     return filepath
