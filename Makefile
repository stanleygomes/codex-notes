help:
	@echo "Available Makefile commands:"
	@echo "  make build         - Build the project"
	@echo "  make build-plugin  - Build the IntelliJ plugin"
	@echo "  make verify-plugin - Verify plugin compatibility"
	@echo "  make test          - Run the tests"
	@echo "  make test-coverage - Run tests with coverage report"
	@echo "  make codecov       - Upload coverage report to Codecov"
	@echo "  make inspections   - Run Qodana code inspections"
	@echo "  make run           - Launch IntelliJ with the plugin for testing"

build:
	./gradlew build

build-plugin:
	./gradlew buildPlugin

verify-plugin:
	./gradlew verifyPlugin

test:
	./gradlew test

test-coverage:
	./gradlew koverHtmlReport
	@echo ""; \
	echo "╔═══════════════════════════════════════════════════════════════╗"; \
	echo "║              Coverage Summary Report (Console)                ║"; \
	echo "╠═══════════════════════════════════════════════════════════════╣"; \
	KOVER_OUTPUT=$$(./gradlew koverLog 2>&1); \
	LINE=$$(echo "$$KOVER_OUTPUT" | grep "application line coverage:" | grep -o '[0-9]*\.[0-9]*' | head -1); \
	printf "║ %-25s │ %10s%%                        ║\n" "Line Coverage" "$$LINE"; \
	echo "╠═══════════════════════════════════════════════════════════════╣"; \
	if [ -n "$$LINE" ]; then \
		LINE_INT=$$(echo "$$LINE" | cut -d. -f1); \
		if [ "$$LINE_INT" -ge 90 ]; then \
			printf "║ \033[32m✅ Status: PASSED - Coverage above 90%%!\033[0m                     ║\n"; \
		else \
			printf "║ \033[31m❌ Status: FAILED - Coverage below 90%%!\033[0m                     ║\n"; \
		fi; \
	else \
		printf "║ \033[31m❌ Status: ERROR - Could not determine coverage!\033[0m          ║\n"; \
	fi; \
	echo "╠═══════════════════════════════════════════════════════════════╣"; \
	echo "║ Note: Full report with all metrics (Class, Method, Branch,   ║"; \
	echo "║       Line, Instruction) available in HTML report below.     ║"; \
	echo "╚═══════════════════════════════════════════════════════════════╝"; \
	echo ""; \
	echo "Open coverage report: file://$(PWD)/build/reports/kover/html/index.html"

inspections:
	./gradlew runInspections

run:
	./gradlew runIde

check:
	./gradlew ktlintCheck --console=plain

format:
	./gradlew ktlintFormat --console=plain
