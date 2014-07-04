SHELL := /bin/bash

deploy:
	-git subtree split --prefix server -b deploy
	git checkout deploy
	git add -f server/conf/application.conf
	git mv server/conf/application.conf conf/
	git commit -m "add conf"
	git push clever deploy:master
	git mv conf/application.conf server/conf/
	git checkout master

.PHONY: deploy
