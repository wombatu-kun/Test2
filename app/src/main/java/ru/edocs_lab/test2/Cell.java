package ru.edocs_lab.test2;

class Cell {
    private Cell environment[];
    private CellContent content;

    void setEnvironment(Cell environ[]) { this.environment = environ; }

    Cell[] getEnvironment() {
        return environment;
    }

    boolean go(Direction randomDir, long timestamp) {
        return content.go(this, randomDir, timestamp);
    }

    CellContent getContent() {
        return content;
    }

    void setContent(CellContent content) {
        this.content = content;
    }
}
