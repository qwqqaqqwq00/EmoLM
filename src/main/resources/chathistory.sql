DROP TABLE IF EXISTS chat_history;
CREATE TABLE chat_history (
      id INT AUTO_INCREMENT PRIMARY KEY,
      uid INT NOT NULL,
      hid INT NOT NULL,
      message TEXT NOT NULL,
      role VARCHAR(50) NOT NULL,
      timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- Modified: Changed BIGINT to TIMESTAMP and set default value to CURRENT_TIMESTAMP
);