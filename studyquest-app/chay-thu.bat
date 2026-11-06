@echo off
title StudyQuest Web App
chcp 65001 >nul
echo ====================================================
echo   Đang khởi động StudyQuest Mobile Web App...
echo   Địa chỉ: http://localhost:3000/
echo ====================================================
start http://localhost:3000/
powershell -ExecutionPolicy Bypass -File "%~dp0studyquest-app\serve.ps1"
pause
