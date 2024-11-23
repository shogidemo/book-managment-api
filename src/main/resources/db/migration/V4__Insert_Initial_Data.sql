INSERT INTO authors (name, birth_date) VALUES
('山田 太郎', '1980-01-01'),
('佐藤 花子', '1975-06-15');

INSERT INTO books (title, price, published) VALUES
('初めてのプログラミング', 2000.00, FALSE),
('詳細Java解説', 3500.00, TRUE);

INSERT INTO books_authors (book_id, author_id) VALUES
(1, 1), -- 初めてのプログラミング - 山田 太郎
(2, 1), -- 詳細Java解説 - 山田 太郎
(2, 2); -- 詳細Java解説 - 佐藤 花子
