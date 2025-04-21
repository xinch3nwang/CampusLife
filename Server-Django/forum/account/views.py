import json

from django.core import serializers

from .models import Usr, UsrProfile
from django.http import JsonResponse, HttpResponse
from django.core.exceptions import ObjectDoesNotExist


# Create your views here.
def signup(request):
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')
        nickname = username  # request.POST.get('nickname')
        info_success = {
            'username': username,
            'code': 1
        }
        info_error = {
            'username': username,
            'code': 0
        }
        try:
            # User.objects.create_user(username=username, password=password)
            Usr.objects.get(username=username)
        except ObjectDoesNotExist:
            Usr.objects.create(username=username, password=password, nickname=nickname)
            info_success['id'] = Usr.objects.get(username=username).id

            UsrProfile.objects.create(usr=Usr.objects.get(username=username))
            return JsonResponse(info_success)
        else:
            info_error['id'] = Usr.objects.get(username=username).id
            return JsonResponse(info_error)
    else:
        return


def login(request):
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')
        info_success = {
            'username': username,
            'code': 1
        }
        info_miss = {
            'username': username,
            'code': 2,
            'msg': '用户不存在'
        }
        info_wrong = {
            'username': username,
            'code': 3,
            'msg': '密码错误'
        }
        try:
            usr = Usr.objects.get(username=username)
            info_success['id'] = Usr.objects.get(username=username).id
        except ObjectDoesNotExist:
            return JsonResponse(info_miss)
        else:
            if usr.password == password:
                return JsonResponse(info_success)
            else:
                return JsonResponse(info_wrong)
    else:
        return


def get_id_nickname(request):
    nickname = request.POST.get('nickname')
    usr = Usr.objects.get(nickname=nickname)
    return HttpResponse(usr.id)


def user_profile(request):
    """用户详细资料"""
    user_id = request.POST.get('usrid')
    user = Usr.objects.get(pk=user_id)
    usr_profile = UsrProfile.objects.filter(usr_id=user.id)
    data = serializers.serialize("json", usr_profile)  # str
    list_json = json.loads(data)  # list
    list_json[0]['nickname'] = user.nickname

    return JsonResponse({'code': '200',
                         'count': len(list_json),
                         'msg': '查看所选用户详细资料',
                         'result': list_json})


def user_profile_update(request):
    """修改用户详细资料"""
    user_id = request.POST.get('usrid')
    user = Usr.objects.get(pk=user_id)
    user.nickname = request.POST.get('nickname')
    usr_profile = UsrProfile.objects.get(usr_id=user.id)
    usr_profile.age = int(request.POST.get('age'))
    usr_profile.gender = request.POST.get('gender')
    usr_profile.email = request.POST.get('email')
    usr_profile.status = request.POST.get('status')
    usr_profile.intro = request.POST.get('intro')
    user.save()
    usr_profile.save()
    return JsonResponse({'code': '200'})


def user_follow(request):
    """关注用户"""
    fan_id = request.POST.get('fanid')
    author_nickname = request.POST.get('authornickname')
    author_id = Usr.objects.get(nickname=author_nickname[:-1]).id
    author = UsrProfile.objects.get(usr_id=author_id)
    fan = UsrProfile.objects.get(usr_id=fan_id)

    if author not in fan.usrs_liked.all():  # 原本未关注 关注
        fan.usrs_liked.add(author)
        author.fans.add(fan)
        fan.usrs_liked_count += 1
        author.fans_count += 1
        fan.save()
        author.save()
        return HttpResponse("f")
    else:  # 原本关注 取关
        fan.usrs_liked.remove(author)
        author.fans.remove(fan)
        fan.usrs_liked_count -= 1
        author.fans_count -= 1
        fan.save()
        author.save()
        return HttpResponse("un")


def follow_users(request):
    """查看关注的用户"""
    user_id = request.POST.get('usrid')
    user_profile = UsrProfile.objects.get(usr_id=user_id)
    return HttpResponse(user_profile.usrs_liked.all())


def fans(request):
    """查看粉丝"""
    user_id = request.POST.get('usrid')
    user_profile = UsrProfile.objects.get(usr_id=user_id)
    return HttpResponse(user_profile.fans.all())
