SHELL := /bin/bash

deploy:
	-git subtree split --prefix server -b deploy
	git push -f heroku deploy:master
	git branch -D deploy

.PHONY: deploy
