from django.http import JsonResponse, HttpResponse
from rest_framework.response import Response

from account.models import Usr
from .models import Comment
from article.models import Article
from .serializer import CommentModelSerializer
from rest_framework.response import Response
from rest_framework.views import APIView


# Create your views here.
def comment_publish(request):
    article_id = request.POST.get('article_id')
    parent_id = request.POST.get('parent_id')
    content = request.POST.get('content')
    user_id = request.POST.get('user_id')
    Comment.objects.create(article_id=article_id, parent_id=parent_id, content=content, user_id=user_id)
    return HttpResponse("pub")


def comment_delete(request):
    comment_id = request.POST.get('comment_id')
    Article.objects.get(pk=comment_id).delete()
    return HttpResponse("del")


class CommentAPIView(APIView):
    def get(self, request):
        article_id = request.POST.get('article_id')
        comment = Comment.objects.filter(article_id=article_id)
        comment_serializer = CommentModelSerializer(comment, many=True)
        # return Response(comment_serializer.data)
        data = comment_serializer.data
        for i in range(len(data)):
            data[i]['nickname'] = Usr.objects.get(pk=data[i]['user']).nickname
        return JsonResponse({
            'count': len(data),
            'result': data
        })
    def post(self, request):
        article_id = request.POST.get('article_id')
        parent = request.POST.get('parent_id')
        if parent is None:
            comment = Comment.objects.filter(article_id=article_id, parent__isnull=True)
            comment_serializer = CommentModelSerializer(comment, many=True)
            data = comment_serializer.data
            for i in range(len(data)):
                data[i]['nickname'] = Usr.objects.get(pk=data[i]['user']).nickname
            return JsonResponse({
                'count': len(data),
                'result': data
            })
        else:
            comment = Comment.objects.filter(article_id=article_id, parent=parent)
            comment_serializer = CommentModelSerializer(comment, many=True)
            data = comment_serializer.data
            for i in range(len(data)):
                data[i]['nickname'] = Usr.objects.get(pk=data[i]['user']).nickname
            return JsonResponse({
                'count': len(data),
                'result': data
            })
