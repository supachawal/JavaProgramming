#include <stdio.h>
#include <stdlib.h>
#include <sys/queue.h>

struct Q_Entry {
    char c;
    SIMPLEQ_ENTRY(Q_Entry) q_entries;
};

typedef SIMPLEQ_HEAD(SimpleQHead, Q_Entry) Q_Head;

void appendQueue(Q_Head *qHeadPtr, char ch) {
    struct Q_Entry *elem = malloc(sizeof(struct Q_Entry));
    if (elem) {
        elem->c = ch;
    }
    SIMPLEQ_INSERT_TAIL(qHeadPtr, elem, q_entries);
}

int main(int argc, char *argv[]) {
    char ch = 'A';
    int i;
    struct Q_Entry *elem;
    Q_Head qHead;

    SIMPLEQ_INIT(&qHead);
    for (i=0; i<4; i++)
        appendQueue(&qHead, ch++);

    i = 1;
    while (!SIMPLEQ_EMPTY(&qHead)) {
        elem = SIMPLEQ_FIRST(&qHead);
        printf("%c ", elem->c);
        SIMPLEQ_REMOVE_HEAD(&qHead, q_entries);
        free(elem);
        if (++i == 2)
            appendQueue(&qHead, 'Z');
    }
}
