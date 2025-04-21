from django.urls import path,re_path 
from comment import views


urlpatterns = [ 
    path('comment_publish/', views.comment_publish),
    path('comment_delete/', views.comment_delete),
    path('comment_get/', views.CommentAPIView.as_view(), name='comment_get'),
] 