tmux: SESSION_NAME := eventchain
tmux:
	@tmux new-session -d -s $(SESSION_NAME)
	@tmux source-file $(shell pwd)/.tmux.conf
	@tmux attach -t ${SESSION_NAME}
.PHONY: tmux
