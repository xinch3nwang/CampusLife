from django.urls import path,re_path 
from article import views


urlpatterns = [ 
    path('article_publish/', views.article_publish),
    path('article_update/', views.article_update),
    path('article_delete/', views.article_delete),
    path('article_detail/', views.article_detail),
    path('article_like/', views.article_like),
    path('article_collect/', views.article_collect),
    path('articles_author/', views.articles_author),
    path('articles_list/', views.articles_list),
    path('articles_tag/', views.articles_tag),
    path('articles_date/', views.articles_date),
    path('articles_today/', views.articles_today),

]
