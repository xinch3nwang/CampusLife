from django.http import JsonResponse, HttpResponse
from rest_framework.response import Response

from account.models import Usr
from .models import Comment
from article.models import Article
from .serializer import CommentModelSerializer
from rest_framework.response import Response
from rest_framework.views import APIView


# Create your views here.
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated

@api_view(['POST'])
@permission_classes([IsAuthenticated])
def comment_publish(request):
    try:
        article_id = request.POST.get('article_id')
        parent_id = request.POST.get('parent_id')
        content = request.POST.get('content')
        user_id = request.POST.get('user_id')
        
        if not all([article_id, content, user_id]):
            return JsonResponse({
                'code': 400,
                'error': 'Missing required fields'
            }, status=400)
            
        comment = Comment.objects.create(
            article_id=article_id,
            parent_id=parent_id,
            content=content,
            user_id=user_id
        )
        
        return JsonResponse({
            'code': 200,
            'message': 'Comment published successfully',
            'comment_id': comment.id
        })
        
    except Exception as e:
        return JsonResponse({
            'code': 500,
            'error': str(e)
        }, status=500)


@api_view(['POST'])
@permission_classes([IsAuthenticated])
def comment_delete(request):
    try:
        comment_id = request.POST.get('comment_id')
        if not comment_id:
            return JsonResponse({
                'code': 400,
                'error': 'comment_id is required'
            }, status=400)
            
        Comment.objects.get(pk=comment_id).delete()
        
        return JsonResponse({
            'code': 200,
            'message': 'Comment deleted successfully'
        })
        
    except Comment.DoesNotExist:
        return JsonResponse({
            'code': 404,
            'error': 'Comment not found'
        }, status=404)
    except Exception as e:
        return JsonResponse({
            'code': 500,
            'error': str(e)
        }, status=500)


from rest_framework.permissions import IsAuthenticated

class CommentAPIView(APIView):
    permission_classes = [IsAuthenticated]
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
