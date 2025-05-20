DROP TABLE IF EXISTS chat_history_titles;
CREATE TABLE chat_history_titles (
      id INT AUTO_INCREMENT PRIMARY KEY,
      uid INT NOT NULL,
      hid INT NOT NULL,
      title TEXT NOT NULL
);