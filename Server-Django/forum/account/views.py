import json

from django.core import serializers

from .models import Usr, UsrProfile
from django.http import JsonResponse, HttpResponse
from django.core.exceptions import ObjectDoesNotExist
from django.db import transaction


# Create your views here.
from rest_framework_simplejwt.tokens import RefreshToken


def signup(request):
    # print("Request POST data:", request.POST)
    if request.method != 'POST':
        return JsonResponse({'error': 'Method not allowed'}, status=405)

    try:
        username = request.POST['username']
        password = request.POST['password']
        # 验证输入有效性
        if not username or not password:
            return JsonResponse({
                'error': 'Username and password are required',
                'code': 400
            }, status=400)
            
        if len(password) < 8:
            return JsonResponse({
                'error': 'Password must be at least 8 characters',
                'code': 400
            }, status=400)

        # 检查用户是否存在
        if Usr.objects.filter(username=username).exists():
            return JsonResponse({
                'error': 'Username already exists',
                'code': 409
            }, status=409)
        # print(username)
        # 创建用户和profile（使用事务保证原子性）
        with transaction.atomic():
            user = Usr.objects.create(
                username=username,
                password=password,
                nickname=username
            )
            UsrProfile.objects.create(usr=user)
            
            # 返回创建成功的响应
            return JsonResponse({
                'id': user.id,
                'username': username,
                'code': 201,
                'message': 'User created successfully'
            }, status=201)

    except KeyError as e:
        return JsonResponse({
            'error': f'Missing required field: {str(e)}',
            'code': 400
        }, status=400)
    except Exception as e:
        return JsonResponse({
            'error': 'Internal server error',
            'code': 500
        }, status=500)
    # if request.method == 'POST':
    #     username = request.POST.get('username')
    #     password = request.POST.get('password')
    #     nickname = username  # request.POST.get('nickname')
    #     info_success = {
    #         'username': username,
    #         'code': 1
    #     }
    #     info_error = {
    #         'username': username,
    #         'code': 0
    #     }
    #     try:
    #         # User.objects.create_user(username=username, password=password)
    #         Usr.objects.get(username=username)
    #     except ObjectDoesNotExist:
    #         Usr.objects.create(username=username, password=password, nickname=nickname)
    #         info_success['id'] = Usr.objects.get(username=username).id

    #         UsrProfile.objects.create(usr=Usr.objects.get(username=username))
    #         return JsonResponse(info_success)
    #     else:
    #         info_error['id'] = Usr.objects.get(username=username).id
    #         return JsonResponse(info_error)
    # else:
    #     return

def login(request):
    if request.method != 'POST':
        return JsonResponse({'error': 'Method not allowed'}, status=405)

    try:
        username = request.POST['username']
        password = request.POST['password']
        # 验证输入有效性
        if not username or not password:
            return JsonResponse({
                'error': 'Username and password are required',
                'code': 400
            }, status=400)

        user = Usr.objects.get(username=username)
        if user.password != password:
            return JsonResponse({
                'error': 'Invalid credentials',
                'code': 401
            }, status=401)

        refresh = RefreshToken.for_user(user)
        return JsonResponse({
            'refresh': str(refresh),
            'access': str(refresh.access_token),
            'code': 200
        }, status=200)

        try:
            user = Usr.objects.get(username=username)
            if user.password == password:
                return JsonResponse({
                    'id': user.id,
                    'username': username,
                    'code': 200,
                    'message': 'Login successful'
                })
            else:
                return JsonResponse({
                    'error': 'Invalid password',
                    'code': 401
                }, status=401)
        except ObjectDoesNotExist:
            return JsonResponse({
                'error': 'User not found',
                'code': 404
            }, status=404)
    except KeyError as e:
        return JsonResponse({
            'error': f'Missing required field: {str(e)}',
            'code': 400
        }, status=400)
    except Exception as e:
        return JsonResponse({
            'error': 'Internal server error',
            'code': 500
        }, status=500)

def get_id_nickname(request):
    if request.method != 'GET':
        return JsonResponse({'error': 'Method not allowed'}, status=405)
    
    try:
        nickname = request.GET['nickname']
        usr = Usr.objects.get(nickname=nickname)
        return JsonResponse({'id': usr.id}, status=200)
    except KeyError:
        return JsonResponse({'error': 'Nickname parameter is required'}, status=400)
    except ObjectDoesNotExist:
        return JsonResponse({'error': 'User not found'}, status=404)
    except Exception:
        return JsonResponse({'error': 'Internal server error'}, status=500)

def user_profile(request):
    # print("Request GET data:", request.GET)
    """用户详细资料"""
    if request.method != 'GET':
        return JsonResponse({'error': 'Method not allowed'}, status=405)
    
    try:
        user_id = request.GET['usrid']
        user = Usr.objects.get(pk=user_id)
        usr_profile = UsrProfile.objects.get(usr_id=user.id)
        
        profile_data = {
            'nickname': user.nickname,
            'age': usr_profile.age,
            'gender': usr_profile.gender,
            'email': usr_profile.email,
            'status': usr_profile.status,
            'intro': usr_profile.intro,
            'fans_count': usr_profile.fans_count,
            'usrs_liked_count': usr_profile.usrs_liked_count
        }
        
        return JsonResponse({
            'code': 200,
            'data': profile_data,
            'message': 'User profile retrieved successfully'
        })
    except KeyError:
        return JsonResponse({'error': 'User ID parameter is required'}, status=400)
    except ObjectDoesNotExist:
        return JsonResponse({'error': 'User not found'}, status=404)
    except Exception:
        return JsonResponse({'error': 'Internal server error'}, status=500)

def user_profile_update(request):
    """修改用户详细资料"""
    if request.method != 'POST':
        return JsonResponse({'error': 'Method not allowed'}, status=405)
    
    try:
        user_id = request.POST['usrid']
        user = Usr.objects.get(pk=user_id)
        usr_profile = UsrProfile.objects.get(usr_id=user.id)
        
        with transaction.atomic():
            if 'nickname' in request.POST:
                user.nickname = request.POST['nickname']
            if 'age' in request.POST:
                usr_profile.age = int(request.POST['age'])
            if 'gender' in request.POST:
                usr_profile.gender = request.POST['gender']
            if 'email' in request.POST:
                usr_profile.email = request.POST['email']
            if 'status' in request.POST:
                usr_profile.status = request.POST['status']
            if 'intro' in request.POST:
                usr_profile.intro = request.POST['intro']
            
            user.save()
            usr_profile.save()
            
            return JsonResponse({
                'code': 200,
                'message': 'Profile updated successfully'
            })
    except KeyError:
        return JsonResponse({'error': 'User ID parameter is required'}, status=400)
    except ObjectDoesNotExist:
        return JsonResponse({'error': 'User not found'}, status=404)
    except ValueError:
        return JsonResponse({'error': 'Invalid parameter value'}, status=400)
    except Exception:
        return JsonResponse({'error': 'Internal server error'}, status=500)

def user_follow(request):
    """关注/取消关注用户"""
    if request.method != 'POST':
        return JsonResponse({'error': 'Method not allowed'}, status=405)

    try:
        fan_id = request.POST['fanid']
        author_nickname = request.POST['authornickname']
        
        with transaction.atomic():
            author = UsrProfile.objects.get(usr__nickname=author_nickname)
            fan = UsrProfile.objects.get(usr_id=fan_id)

            if author not in fan.usrs_liked.all():
                fan.usrs_liked.add(author)
                author.fans.add(fan)
                fan.usrs_liked_count += 1
                author.fans_count += 1
                action = 'followed'
            else:
                fan.usrs_liked.remove(author)
                author.fans.remove(fan)
                fan.usrs_liked_count -= 1
                author.fans_count -= 1
                action = 'unfollowed'
            
            fan.save()
            author.save()
            
            return JsonResponse({
                'code': 200,
                'action': action,
                'fans_count': author.fans_count,
                'following_count': fan.usrs_liked_count
            })
            
    except KeyError:
        return JsonResponse({'error': 'Missing required parameters'}, status=400)
    except ObjectDoesNotExist:
        return JsonResponse({'error': 'User not found'}, status=404)
    except Exception:
        return JsonResponse({'error': 'Internal server error'}, status=500)

def follow_users(request):
    """查看关注的用户列表"""
    if request.method != 'GET':
        return JsonResponse({'error': 'Method not allowed'}, status=405)

    try:
        user_id = request.GET['usrid']
        user_profile = UsrProfile.objects.get(usr_id=user_id)
        following = [
            {
                'id': profile.usr.id,
                'nickname': profile.usr.nickname
            }
            for profile in user_profile.usrs_liked.all()
        ]
        
        return JsonResponse({
            'code': 200,
            'count': len(following),
            'following': following
        })
        
    except KeyError:
        return JsonResponse({'error': 'User ID parameter is required'}, status=400)
    except ObjectDoesNotExist:
        return JsonResponse({'error': 'User not found'}, status=404)
    except Exception:
        return JsonResponse({'error': 'Internal server error'}, status=500)

def fans(request):
    """查看粉丝列表"""
    if request.method != 'GET':
        return JsonResponse({'error': 'Method not allowed'}, status=405)

    try:
        user_id = request.GET['usrid']
        user_profile = UsrProfile.objects.get(usr_id=user_id)
        fans_list = [
            {
                'id': fan.usr.id,
                'nickname': fan.usr.nickname
            }
            for fan in user_profile.fans.all()
        ]
        
        return JsonResponse({
            'code': 200,
            'count': len(fans_list),
            'fans': fans_list
        })
        
    except KeyError:
        return JsonResponse({'error': 'User ID parameter is required'}, status=400)
    except ObjectDoesNotExist:
        return JsonResponse({'error': 'User not found'}, status=404)
    except Exception:
        return JsonResponse({'error': 'Internal server error'}, status=500)
