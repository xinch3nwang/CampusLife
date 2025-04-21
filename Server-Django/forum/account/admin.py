from django.contrib import admin
from .models import Usr, UsrProfile


# Register your models here.
# class UserAdmin(admin.ModelAdmin):
#     list_display = ('username', 'nickname', 'status', 'gender')
#     list_filter = ('status', 'create_time',)
#     list_per_page = 10


admin.site.register(Usr)
admin.site.register(UsrProfile)

admin.site.site_header = "学生社区管理系统"
admin.site.site_title = "学生社区管理系统"
admin.site.index_title = "学生社区管理系统"
