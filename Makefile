all-up:
	docker-compose up

infra-up:
	docker-compose up -d database data_platform

infra-down:
	docker-compose down

setup:
	! grep -qw data_platform /etc/hosts && sudo sh -c 'echo "127.0.0.1 data_platform" >> /etc/hosts'
	! grep -qw database /etc/hosts && sudo sh -c 'echo "127.0.0.1 database" >> /etc/hosts'
	echo SPRING_PROFILES_ACTIVE=local >> .env
