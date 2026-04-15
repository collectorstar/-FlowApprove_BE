param(
    [string]$Action = "migrate",
    [string]$Tenant = "all"
)

$env:FLOWAPPROVE_DB_ACTION = $Action
$env:FLOWAPPROVE_DB_TENANT = $Tenant

& .\mvnw.cmd -pl flowapprove-db-migrator -am spring-boot:run
