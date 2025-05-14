from rest_framework import serializers
from .models import Usr, UsrProfile

class UserSignupSerializer(serializers.ModelSerializer):
    class Meta:
        model = Usr
        fields = ['username', 'password', 'nickname']
        extra_kwargs = {'password': {'write_only': True}}

    def validate_password(self, value):
        if len(value) < 8:
            raise serializers.ValidationError("Password must be at least 8 characters")
        return value

class UserLoginSerializer(serializers.Serializer):
    username = serializers.CharField()
    password = serializers.CharField(write_only=True)