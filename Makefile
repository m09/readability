SHELL := /bin/bash

deploy:
	-git subtree split --prefix server -b deploy
	git push -f heroku deploy:master

.PHONY: deploy
