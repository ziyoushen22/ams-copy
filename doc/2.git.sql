

-- gitlab
-- ex-maoyongqiang001 Pass@word1
-- 与远程仓库建立连接
git remote add origin **.git
-- 设置用户名和密码
git config --global user.name "ex-maoyongqiang001"
git config --global user.email "Pass@word1"
--清除缓存
git rm -r --cached ./log
--初始化本地仓库
git init
--其他
git pull
git push -u origin master
git clone http://name:pass@**.git
git commit -m "**"

git branch mao      --创建新分支
git checkout mao    --切换到新分支
git checkout -b mao --上面两个命令也可以合成为一个命令

