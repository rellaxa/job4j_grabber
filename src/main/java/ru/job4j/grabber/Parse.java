package ru.job4j.grabber;

import java.io.IOException;
import java.util.List;

public interface Parse {
    List<Post> list(String link);

    List<Post> parse();
}
