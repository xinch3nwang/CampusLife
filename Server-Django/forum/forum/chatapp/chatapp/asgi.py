"""
ASGI config for chatapp project.

It exposes the ASGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/3.2/howto/deployment/asgi/
"""

import os
import sys

from django.core.asgi import get_asgi_application
from channels.routing import ProtocolTypeRouter, URLRouter
from channels.auth import AuthMiddlewareStack
from forum.chatapp.chat import routing

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'chatapp.settings')

application = ProtocolTypeRouter({

    "http": get_asgi_application(),

    # 在这里指定websocket协议交由特定的路由来处理
    "websocket": AuthMiddlewareStack(
        URLRouter(
            routing.websocket_urlpatterns
        )
    )
})
