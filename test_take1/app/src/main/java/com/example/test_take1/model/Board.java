package com.example.test_take1.model;

import com.example.test_take1.model.Cell.E_STATUS;
import static java.lang.Math.abs;

public class Board {

    public static final int COLS = 8;
    public static final int ROWS = 8;

    private int width;
    private int height;
    private int top;
    private int left;

    private Cell cells[][] = new Cell[ROWS][COLS];
    private Cell.E_STATUS turn;

    public Board() {

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j] = new Cell();
            }
        }

        //初期の配置をセット
        cells[ROWS / 2 - 1][COLS / 2 - 1].setStatus(Cell.E_STATUS.Black);
        cells[ROWS / 2 - 1][COLS / 2].setStatus(Cell.E_STATUS.White);
        cells[ROWS / 2][COLS / 2 - 1].setStatus(Cell.E_STATUS.White);
        cells[ROWS / 2][COLS / 2].setStatus(Cell.E_STATUS.Black);

        turn = Cell.E_STATUS.Black;
    }

    public void setSize(int width, int height) {
        int sz = width < height ? width : height;
        
        setWidth(sz);
        setHeight(sz);
    }

    public void setWidth(int width) {
        this.width = (int) (width / Board.COLS) * Board.COLS;            //列数で割り切れない場合は余りを捨てる。

        float cellW = this.getCellWidth();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j].setWidth(cellW);
                cells[i][j].setLeft((int) (j * cellW));
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = (int) (height / Board.ROWS) * Board.ROWS;        //行数で割り切れない場合は余りを捨てる。

        float cellH = this.getCellHeidht();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j].setHeight(cellH);
                cells[i][j].setTop((int) (i * cellH));
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getTop() {
        return top;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getLeft() {
        return left;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public float getCellWidth() {
        return (float) (this.width / COLS);
    }

    public float getCellHeidht() {
        return (float) (this.height / ROWS);
    }

    public void changeCell(int r, int c, Cell.E_STATUS newStatus) throws Exception {
        Cell cell = cells[r][c];
        if (cell.getStatus() != E_STATUS.None) {
            throw new Exception("Cell is not empty.");
        }
        cell.setStatus(newStatus);
    }

    public Cell.E_STATUS getTurn() {
        return this.turn;
    }

    public void changeTurn() {
        if (this.turn == E_STATUS.Black) {
            this.turn = E_STATUS.White;
        } else {
            this.turn = E_STATUS.Black;
        }
        setCheckCell(this.turn);
    }

    public void reversingCell(int r, int c, Cell.E_STATUS newStatus) {
        for (int vector_row = -1; vector_row < 2; vector_row++) {
            for (int vector_col = -1; vector_col < 2; vector_col++) {
                int space_row = 1;
                int space_col = 1;
                int lookup = 0;
                if (vector_col == -1) space_col = c;
                if (vector_col == 1) space_col = COLS - 1 - c;
                if (vector_row == -1) space_row = r;
                if (vector_row == 1) space_row = ROWS - 1 - r;
                if (space_row > 0 & space_col > 0) {
                    if (cells[r + vector_row][c + vector_col].getStatus() != newStatus) {
                        while (space_row - abs(vector_row * lookup) > 0 & space_col - abs(vector_col * lookup) > 0) {
                            lookup++;
                            if (cells[r + vector_row * lookup][c + vector_col * lookup].getStatus() == E_STATUS.None)
                                break;
                            else if (cells[r + vector_row * lookup][c + vector_col * lookup].getStatus() == newStatus) {
                                for (int i = 1; i < lookup; i++) {
                                    cells[r + vector_row * i][c + vector_col * i].setStatus(newStatus);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public Cell.PUT_STATUS getCheckCell(int r, int c) {return cells[r][c].getCheckStatus();}

    public void setCheckCell(Cell.E_STATUS newStatus) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                cells[r][c].setCheckStatus(Cell.PUT_STATUS.Cannot);
                if (cells[r][c].getStatus() == E_STATUS.None) {
                    for (int vector_row = -1; vector_row < 2; vector_row++) {
                        for (int vector_col = -1; vector_col < 2; vector_col++) {
                            int space_row = 1;
                            int space_col = 1;
                            int lookup = 0;
                            if (vector_col == -1) space_col = c;
                            if (vector_col == 1) space_col = COLS - 1 - c;
                            if (vector_row == -1) space_row = r;
                            if (vector_row == 1) space_row = ROWS - 1 - r;
                            if (space_row > 0 & space_col > 0) {
                                if (cells[r + vector_row][c + vector_col].getStatus() != newStatus) {
                                    while (space_row - abs(vector_row * lookup) > 0 & space_col - abs(vector_col * lookup) > 0) {
                                        lookup++;
                                        if (cells[r + vector_row * lookup][c + vector_col * lookup].getStatus() == E_STATUS.None)
                                            break;
                                        else if (cells[r + vector_row * lookup][c + vector_col * lookup].getStatus() == newStatus) {
                                            cells[r][c].setCheckStatus(Cell.PUT_STATUS.Can);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

    /*
    public void reversingCell(int r, int c, Cell.E_STATUS newStatus) {
        int row_vector = -1; //検索する方向（行）
        int col_vector = -1; //検索する方向（列）
        while (col_vector <= 1) {
            while (row_vector <= 1) {
                if (0 <= r + row_vector && r + row_vector <= ROWS - 1 && 0 <= c + col_vector && c + col_vector <= COLS - 1) { //探索する1つ目のセルが存在するかチェック
                    if (cells[r + row_vector][c + col_vector].getStatus() != newStatus) { //探索する1つ目のセルが自分と同じ色でないことをチェック
                        int row_lookup = row_vector;
                        int col_lookup = col_vector;
                        while (0 <= r + row_lookup && r + row_lookup <= ROWS - 1 && 0 <= c + col_lookup && c + col_lookup <= COLS - 1) { //探索するセルが存在する限り繰り返し
                            if (cells[r + row_lookup][c + col_lookup].getStatus() == E_STATUS.None)
                                break; //探索しているセルが空欄だったら，この方向のセル探索を打ち切り
                            else if (cells[r + row_lookup][c + col_lookup].getStatus() == newStatus) { //ひっくり返す起点を見付けたら
                                int i = 0;
                                do {
                                    cells[r + i * row_vector][c + i * col_vector].setStatus(newStatus);
                                    i++;
                                } while (i < row_lookup * row_vector || i < col_lookup * col_vector); //その起点にたどり着くまでひっくり返し続ける
                                break; //ひっくり返し終わったらこの方向のセル探索を打ち切り
                            }
                            row_lookup = row_lookup + row_vector;
                            col_lookup = col_lookup + col_vector; // 置かれた石と同じ色だった場合は，隣のセルに移行
                        }
                    }
                }
                row_vector++; //検索する方向（行）を変更
            }
            row_vector = -1; //検索する方向（行）をリセット
            col_vector++; //検索する方向（列）を変更
        }
    }
}
*/

/*
    public void reversingCell(int r, int c, Cell.E_STATUS newStatus){

        int flag = 0;
        int lookup = 0;
        if(c>=COLS || cells[r][c+1].getStatus() == newStatus) {
            flag = -1;
        }
        while(flag==0 && c+lookup<=COLS) {
            lookup++;
            if(cells[r][c+lookup].getStatus() == E_STATUS.None) {
                flag=-1;
            }else if(cells[r][c+lookup].getStatus() == newStatus) {
                flag=1;
            }
        }
        if(flag==1){
            for(int i=c;i<c+lookup;i++){
                cells[r][i].setStatus(newStatus);
            }
        }
    }
}
*/
//没データ
/*
    public void reversingCell(int r, int c, Cell.E_STATUS ownStatus) {
        Cell cell = cells[r][c];
        Cell.E_STATUS st = cell.getStatus();
        int max_w = c;

        for(int i = 1; i <= max_w-1; i++){
            Cell nextCell = cells[r][c-i];
            Cell.E_STATUS next_st = nextCell.getStatus();
            if(next_st==E_STATUS.None) {break;}
            else if (next_st != st){
                continue;
            }
            else if(next_st==st) {
                for (int j = 1; j <= i; j++) {
                    Cell reCell = cells[r][c - j];
                    reCell.setStatus(ownStatus);
                }
                break;
            }
        }
    }
}
*/

