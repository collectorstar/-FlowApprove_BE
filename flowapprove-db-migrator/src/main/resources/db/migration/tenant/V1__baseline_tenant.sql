create table if not exists workflow_definitions (
    id uuid primary key,
    organization_id uuid not null,
    tenant_id uuid not null,
    name varchar(255) not null,
    status varchar(50) not null,
    version integer not null default 0,
    created_at timestamptz not null default now()
);

create table if not exists workflow_steps (
    id uuid primary key,
    workflow_definition_id uuid not null references workflow_definitions(id),
    name varchar(255) not null,
    approver_role varchar(100) not null,
    order_index integer not null,
    created_at timestamptz not null default now()
);

create table if not exists workflow_requests (
    id uuid primary key,
    workflow_definition_id uuid not null references workflow_definitions(id),
    organization_id uuid not null,
    tenant_id uuid not null,
    requester_id uuid not null,
    title varchar(255) not null,
    status varchar(50) not null,
    submitted_at timestamptz not null,
    created_at timestamptz not null default now()
);

create table if not exists approval_tasks (
    id uuid primary key,
    workflow_request_id uuid not null references workflow_requests(id),
    step_name varchar(255) not null,
    assignee_id uuid not null,
    status varchar(50) not null,
    created_at timestamptz not null default now()
);
