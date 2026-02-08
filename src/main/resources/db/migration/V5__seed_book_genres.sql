-- V5__seed_book_genres.sql
-- Assign one main genre per book

INSERT INTO book_genres (book_id, genre_id)
SELECT b.id, g.id
FROM books b
JOIN genres g
WHERE
(
    b.title IN (
        'Crime and Disorder Bill [H.L.]',
        'Competition Bill [H.L.]'
    ) AND g.genre_name = 'Law'
)
OR (
    b.title IN (
        'Fahrenheit 451','1984','Brave New World',
        'Dune','Foundation','Neuromancer','Snow Crash','The Martian', 'The Handmaid''s Tale'
    ) AND g.genre_name = 'Science Fiction'
)
OR (
    b.title IN (
        'The Hobbit',
        'The Lord of the Rings: The Fellowship of the Ring',
        'The Lord of the Rings: The Two Towers',
        'The Lord of the Rings: The Return of the King',
        'The Chronicles of Narnia: The Lion, the Witch and the Wardrobe',
        'The Chronicles of Narnia: Prince Caspian',
        'The Chronicles of Narnia: The Voyage of the Dawn Treader',
        'The Chronicles of Narnia: The Silver Chair',
        'The Chronicles of Narnia: The Horse and His Boy',
        'The Chronicles of Narnia: The Magician''s Nephew',
        'The Chronicles of Narnia: The Last Battle',
        'Harry Potter and the Sorcerer''s Stone',
        'Harry Potter and the Chamber of Secrets',
        'Harry Potter and the Prisoner of Azkaban',
        'Harry Potter and the Goblet of Fire',
        'Harry Potter and the Order of the Phoenix',
        'Harry Potter and the Half-Blood Prince',
        'Harry Potter and the Deathly Hallows',
        'A Game of Thrones'
    ) AND g.genre_name = 'Fantasy'
)
OR (
    b.title IN (
        'The Shining'
    ) AND g.genre_name = 'Horror'
)
OR (
    b.title IN (
        'The Da Vinci Code','Angels & Demons','The Lost Symbol','Inferno','Origin'
    ) AND g.genre_name = 'Thriller'
)
OR (
    b.title IN (
        'Murder on the Orient Express',
        'The Murder of Roger Ackroyd',
        'The Secret of the Old Clock',
        'The Hidden Staircase',
        'The Bungalow Mystery',
        'The Mystery at Lilac Inn',
        'The Secret of Shadow Ranch'
    ) AND g.genre_name = 'Mystery'
)
OR (
    b.title IN (
        'The Maze Runner','Divergent','The Hunger Games',
        'Thirteen Reasons Why','Looking for Alaska',
        'The Fault in Our Stars','The Perks of Being a Wallflower',
        'The Outsiders','The Giver'
    ) AND g.genre_name = 'Young Adult'
)
OR (
    b.title IN (
        'Alice''s Adventures in Wonderland',
        'Through the Looking-Glass',
        'The Jungle Book',
        'Peter Pan',
        'The Wonderful Wizard of Oz',
        'The Secret Garden',
        'A Little Princess',
        'The Wind in the Willows'
    ) AND g.genre_name = 'Children'
)
OR (
    b.title IN (
        'Don Quixote','The Count of Monte Cristo','Moby Dick',
        'The Adventures of Tom Sawyer','The Adventures of Huckleberry Finn',
        'The Call of the Wild','White Fang',
        'Twenty Thousand Leagues Under the Sea'
    ) AND g.genre_name = 'Adventure'
)
OR (
    b.title IN (
        'War and Peace','The Grapes of Wrath',
        'The Book Thief',
        'The Boy in the Striped Pajamas'
    ) AND g.genre_name = 'Historical Fiction'
)
OR (
    b.title IN (
        'The Diary of a Young Girl'
    ) AND g.genre_name = 'Biography'
)
OR (
    b.title IN (
        'The Divine Comedy'
    ) AND g.genre_name = 'Poetry'
)
OR (
    b.title IN (
        'The Stranger','The Trial','The Metamorphosis',
        'The Sound and the Fury','The Bell Jar',
        'The Color Purple','The Road',
        'One Hundred Years of Solitude',
        'The Picture of Dorian Gray',
        'The Brothers Karamazov',
        'The Old Man and the Sea',
        'The Great Gatsby',
        'To Kill a Mockingbird',
        'Pride and Prejudice',
        'La Celestina',
        'The Little Prince',
        'The Alchemist',
        'The Kite Runner',
        'El guardián invisible',
        'Legado en los huesos',
        'Ofrenda a la tormenta',
        'The Curious Incident of the Dog in the Night-Time',
        'The Einstein Vendetta',
        'Dead Fake',
        'The Oak and the Larch',
        'The Catcher in the Rye'
    ) AND g.genre_name = 'Drama'
);