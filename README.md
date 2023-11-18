# Information Retrieval

## How It Works?

### Register Document

> this part must have a system to **compute the hash** of each file, so we can check duplicate files. then we must give each file an **ID** to point theme in Indexer. another problem is **Large Files**. in Indexer

### Text Process

Text Process consists of two part:

**Tokenize File**: This section extracts Tokens from the text of a file. The Token consists of **Alphabets** and **Digits**

**Remove Duplicates**: This section removes the **Duplicate Tokens** and **sorts** theme **Alphabetically**.

- #### Tokenize Document

    1. **Split** words by **space**

        sample:

        ```txt
        Hi, I'm X. It's my git-hub page. I like reading Irvin.D.Yalom books.
        ```

        output:

        `Hi,` `I'm` `X.` `It's` `my` `git-hub` `page.` `I` `like` `reading` `Irvin.D.Yalom` `books.`

    2. **Extract** each **Alphabets/Digits**

        1. **Remove** every thing after `'`

            ```txt
            I'm => I
            It's => It
            ```

        2. Extract **Alphabets/Digits**

            ```txt
            Hi, => Hi
            X. => X
            git-hub => git hub
            page. => page
            Irvin.D.Yalom => Irvin D Yalom
            ```

        3. **Lowercase** theme

            ```txt
            Hi => hi
            I => i
            X => x
            git hub => it
            page => i
            Irvin D Yalom => irvin d yalom
            ```

    3. **Stem** extracted part of each word by **Porter Stemmer** algorithm

        ```txt
        reading => read
        ```

    4. **Stick** separated parts together

        ```txt
        irvin d yalom => irvindyalom
        ```

    5. **Tokens List**

        `hi` `i` `x` `it` `my` `github` `page` `i` `like` `read` `irvindyalom` `book`

- #### Remove Duplicate Tokens

    We must remove duplicates to create **simple inverted index** of our documents, but we need *Duplicates* to create **positional index** of theme.

    First we create a **copy** of *tokens-list*. Then we can **remove** duplicates. After that, we **sort** the *tokens-list* **Alphabetically**.

    `book` `github` `hi` `i` `irvindyalom` `it` `like` `my` `page` `read` `x`

### Indexer

For now, It supports **Simple Index**, **Biword Index**, **Positional Index**.

- #### Simple Index

- #### Biword Index

- #### Positional Index

### Finder

> this part only **loads index-file** and use its data to retrieve information and find **matched documents**.

### Save Indexer Data on Disk

> this part need some effort to find a suitable file structure to save data. this file structure also should be capable of updating after registering new document or merging to another file.

### Register New Documents

> registering new document is a challenge, there is two differenct approach for that:
>
> 1. updating a saved index-file after adding new doc
> 2. register some new docs and create a new index-file and then, merge old index-file to new one

### CLI

> cli **commands** goes here
