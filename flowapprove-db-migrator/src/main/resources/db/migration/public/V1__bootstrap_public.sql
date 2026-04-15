create table if not exists public.flow_tenants (
    id uuid primary key,
    tenant_code varchar(100) not null unique,
    schema_name varchar(150) not null unique,
    created_at timestamptz not null default now()
);

create table if not exists public.flow_organizations (
    id uuid primary key,
    tenant_id uuid not null references public.flow_tenants(id),
    name varchar(255) not null,
    created_at timestamptz not null default now()
);
