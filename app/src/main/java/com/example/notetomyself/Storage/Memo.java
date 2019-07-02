package com.example.notetomyself.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Memo implements Comparable {

    private String Name;
    private File file;
    private Date time;
    private boolean favorite;
    private List<String> categories;


    public Memo(String name, File file, Date time, boolean favorite, List<String> categories) {
        Name = name;
        this.file = file;
        this.time = time;
        this.favorite = favorite;
        if (categories == null) {
            this.categories = new ArrayList<>();
        } else {
            this.categories = categories;
        }
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Date getTime() {
        return time;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean addCategory(String category) {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        for (String catElememnt: categories) {
            if (catElememnt.equals(category)) return false;
        }
        categories.add(category);
        return true;
    }

    public void removeCategory(String category) {
        if (category != null) {
            categories.remove(category);
        }
    }

    public List<String> getCategories() {
        return categories;
    }

    public boolean isMemberOf(String category) {
        return categories.contains(category);
    }

    @Override
    // 1 = this higher prio
    // -1 = toCompare higher prio
    public int compareTo(Object o) {
        if (!(o instanceof Memo)) return 0;
        Memo toCompare = (Memo) o;

        if (toCompare.isFavorite() && !this.isFavorite()) {
            return 1;
        }
        else if (!toCompare.isFavorite() && this.isFavorite()) {
            return -1;
        }
        else if (toCompare.getTime().after(this.getTime())) {
            return 1;
        } else if (this.getTime().after(toCompare.getTime())) {
            return -1;
        }
        return 0;
    }
}
