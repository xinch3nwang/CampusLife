from django.http import HttpResponse
from django.shortcuts import render
from asgiref.sync import async_to_sync
import channels.layers

# Create your views here.
def index(request):
    return render(request, 'chat/index.html')


def room(request, room_name):
    return render(request, 'chat/room.html', {
        "room_name": room_name
    })


def username(request, user_name):
    channel_layer = channels.layers.get_channel_layer()
    async_to_sync(channel_layer.send)(user_name, {'type': 'hello'})
    d = async_to_sync(channel_layer.receive)(user_name)
    return HttpResponse(list(d.values()))
