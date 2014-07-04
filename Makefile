SHELL := /bin/bash

deploy:
	git-subtree split --prefix server -b deploy

.PHONY: deploy
