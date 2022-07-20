create table declarations (
	index_id text,
	document_id text,
	modifiers integer,
	type_name text,
	source_start integer,
	source_length integer,
	name text,
	signature text
);

create index declarations_index_document on declarations(index_id, document_id);

create table refs (
	index_id text, 
	document_id text, 
	type_moniker text, 
	on_demand boolean,
	name text, 
	signature text, 
	source_start integer, 
	source_end integer
);

create index reference_name on refs(name);

create index reference_index_document on refs(index_id, document_id);