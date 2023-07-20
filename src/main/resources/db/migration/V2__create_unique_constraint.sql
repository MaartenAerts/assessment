create unique index word_relation_unique on word_relation (least(first_word, second_word), greatest(first_word, second_word));
