ALTER TABLE photos
DROP COLUMN thumbnail_url,
DROP COLUMN image_url,
ADD COLUMN thumbnail VARCHAR(255);