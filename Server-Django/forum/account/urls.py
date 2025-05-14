from django.urls import path,re_path 
from account import views
from rest_framework_simplejwt.views import TokenRefreshView


urlpatterns = [ 
    path('signup/', views.signup),
    path('login/', views.login),
    path('get_id_nickname/', views.get_id_nickname),
    path('user_follow/', views.user_follow),
    path('follow_users/', views.follow_users),
    path('fans/', views.fans),
    path('user_profile/', views.user_profile),
    path('user_profile_update/', views.user_profile_update),
    path('token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
]
