SHELL := /bin/bash

deploy:
	git checkout master
	-git branch -D deploy
	git clean -fdx
	git checkout deploy || git checkout --orphan deploy
	mkdir trash
	git mv -k * trash
	git mv trash/server .
	git rm -rf trash
	git mv server/* .
	git add -A
	git commit -m "Deploy"
	git push -f clever deploy:master
	git checkout master
	git clean -fdx

.PHONY: deploy
