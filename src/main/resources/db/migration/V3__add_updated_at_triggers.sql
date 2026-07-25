CREATE OR REPLACE FUNCTION set_updated_at()
       RETURNS TRIGGER AS $$
       BEGIN
            NEW.updated_at = NOW();
            return NEW;
       END;
       $$ LANGUAGE plpgsql;

CREATE TRIGGER handle_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER handle_decks_updated_at
    BEFORE UPDATE ON decks
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER handle_flashcards_updated_at
    BEFORE UPDATE ON flashcards
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();