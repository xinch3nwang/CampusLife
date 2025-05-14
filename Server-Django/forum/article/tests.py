from django.test import TestCase, Client
from account.models import Usr, UsrProfile
from .models import Article
import json
from datetime import datetime, timedelta

class ArticleViewsTest(TestCase):
    def setUp(self):
        self.client = Client()
        self.user = Usr.objects.create(username='testuser', password='testpass', nickname='testuser')
        self.user_profile = UsrProfile.objects.create(usr=self.user)
        self.article = Article.objects.create(
            title='Test Article',
            content='Test Content',
            tag_name='test',
            author=self.user
        )

    def test_article_publish(self):
        response = self.client.post('/article_publish/', {
            'title': 'New Article',
            'content': 'New Content',
            'tagname': 'new',
            'usr_id': self.user.id
        })
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['code'], 200)

    def test_article_update(self):
        response = self.client.post('/article_update/', {
            'title': 'Updated Title',
            'content': 'Updated Content',
            'tagname': 'updated',
            'articleid': self.article.id
        })
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['code'], 200)

    def test_article_delete(self):
        response = self.client.post('/article_delete/', {
            'articleid': self.article.id
        })
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['code'], '200')

    def test_article_detail(self):
        response = self.client.post('/article_detail/', {
            'articleid': self.article.id
        })
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['code'], 200)

    def test_article_like(self):
        response = self.client.post('/article_like/', {
            'article_id': self.article.id,
            'usr_id': self.user.id
        })
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['code'], 200)

    def test_article_collect(self):
        response = self.client.post('/article_collect/', {
            'article_id': self.article.id,
            'usr_id': self.user.id
        })
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['code'], 200)

    def test_articles_list(self):
        response = self.client.get('/articles_list/')
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['code'], '200')

    def test_articles_tag(self):
        response = self.client.post('/articles_tag/', {
            'tagname': 'test'
        })
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['code'], '200')

    def test_articles_author(self):
        response = self.client.post('/articles_author/', {
            'usr_id': self.user.id
        })
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['code'], '200')

    def test_articles_date(self):
        response = self.client.post('/articles_date/', {
            'date': (datetime.now() - timedelta(days=1)).strftime('%Y-%m-%d')
        })
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['code'], '200')

    def test_articles_today(self):
        response = self.client.get('/articles_today/')
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['code'], '200')

    def test_articles_year(self):
        response = self.client.post('/articles_year/', {
            'year': datetime.now().year
        })
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json()['res_code'], '200')
