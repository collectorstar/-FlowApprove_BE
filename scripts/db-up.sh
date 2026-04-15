#!/usr/bin/env bash
set -euo pipefail

ACTION="${1:-migrate}"
TENANT="${2:-all}"

export FLOWAPPROVE_DB_ACTION="$ACTION"
export FLOWAPPROVE_DB_TENANT="$TENANT"

./mvnw -pl flowapprove-db-migrator -am spring-boot:run
