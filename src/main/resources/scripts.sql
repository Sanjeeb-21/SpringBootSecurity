create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);

select * from users;

/*Login with username (email): happy@example.com / password: EazyBytes@12345.*/
INSERT INTO users VALUES ('user', '{noop}EazyBytes@12345', '1')
ON CONFLICT DO NOTHING;

INSERT INTO authorities VALUES ('user', 'read')
ON CONFLICT DO NOTHING;


INSERT INTO users VALUES ('admin', '{bcrypt}$2a$10$yr/hYApWyH3JvhmgaUICPepCJrJs.QQJFaSa3hQ31ioyOXmS/yosa', '1')
ON CONFLICT DO NOTHING;

INSERT INTO authorities VALUES ('admin', 'admin')
ON CONFLICT DO NOTHING;

/*Login with username: user / password: EazyBytes@12345 (noop/plain).*/
INSERT  INTO customer (email, pwd, role) VALUES ('happy@example.com', '{noop}EazyBytes@12345', 'read');

INSERT  INTO customer (email, pwd, role) VALUES ('admin@example.com', '{bcrypt}$2a$10$yr/hYApWyH3JvhmgaUICPepCJrJs.QQJFaSa3hQ31ioyOXmS/yosa', 'admin');