help:
	@echo "Available Makefile commands:"
	@echo "  make build   - Build the project"
	@echo "  make test    - Run the tests"
	@echo "  make run     - Launch IntelliJ with the plugin for testing"

build:
	./gradlew build

test:
	./gradlew test

run:
	./gradlew runIde
