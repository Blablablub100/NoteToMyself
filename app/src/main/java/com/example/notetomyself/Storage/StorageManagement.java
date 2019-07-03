package com.example.notetomyself.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class StorageManagement {

    private static StorageManagement instace;

    private List<Memo> memos;

    private StorageManagement() {
        memos = readSavedData();
    };

    public static StorageManagement getInstance() {
        if (StorageManagement.instace == null) {
            instace = new StorageManagement();
        }
        return instace;
    }

    public List<Memo> getAllMemos() {
        Collections.sort(memos);
        return memos;
    }

    public List<String> getAllCategories() {
        Set<String> categories = new HashSet<>();

        for (Memo memo: memos) {
            List<String> cats = memo.getCategories();
            if (cats != null) {
                categories.addAll(memo.getCategories());
            }
        }
        List<String> res = new ArrayList<>(categories);
        Collections.sort(res);
        return res;
    }

    public List<Memo> getMembersOf(String category) {
        List<Memo> members = new ArrayList<>();
        for (Memo memo: memos) {
            if (memo.isMemberOf(category)) {
                members.add(memo);
            }
        }
        Collections.sort(members);
        return members;
    }

    public List<Memo> getMembersOf(List<String> categories) {
        List<Memo> members = new ArrayList<>();
        for (Memo memo: memos) {
            boolean isMember = true;
            for (String category: categories) {
                if (!memo.isMemberOf(category)) isMember = false;
            }
            if (isMember) members.add(memo);
        }
        return members;
    }

    public void addMemo(String name
            , File file
            , Date time
            , boolean favorite
            , List<String> categories) {
        // TODO check if name exists, file exists, time exists
        // TODO each file must only exist once!
        memos.add(new Memo(name, file, time, favorite, categories));
        writeSavedData();
    }

    public void addMemo(Memo memo) {
        if (!memos.contains(memo)) {
            memos.add(memo);
        }
    }

    public void removeMemo(Memo memo) {
        memos.remove(memo);
        writeSavedData();
    }

    private List<Memo> readSavedData() {
        // TODO READ
        List<Memo> tmp = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(cal.getTimeInMillis() - 10000);
        tmp.add(new Memo("Test1", null, cal.getTime(), false, null));

        cal.setTimeInMillis(cal.getTimeInMillis() - 100000);
        tmp.add(new Memo("Test2", null, cal.getTime(), false, null));

        cal.setTimeInMillis(cal.getTimeInMillis() + 10500);
        tmp.add(new Memo("Test3", null, cal.getTime(), true, null));

        return tmp;
    }

    public void writeSavedData() {
        // TODO WRITE
    }
}
