package com.mycompany.reportaa;import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ReportAA extends JFrame implements ActionListener {
    private static final String[][] SIMILAR_PAIRS = {
        {"か", "が"}, {"さ", "ざ"}, {"た", "だ"}, {"は", "ば"}, {"う", "ぅ"}, {"に", "ぬ"}, {"れ", "ね"}
    };
    private JLabel[] labels; // 表示する文字ラベル
    private JButton generateButton;
    private JTextField inputField;
    private int mistakeIndex; // 間違い文字のインデックス
    private boolean revealed = false; // 色を変えたかどうか
    private Random random = new Random();

    public ReportAA() {
        setTitle("間違い探し");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 上部：文字数入力フィールド
        JPanel topPanel = new JPanel();
        inputField = new JTextField(10);
        generateButton = new JButton("生成");
        generateButton.addActionListener(this);
        topPanel.add(new JLabel("文字数:"));
        topPanel.add(inputField);
        topPanel.add(generateButton);

        // 中央：文字表示パネル（30列×20行のグリッドレイアウト）
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(20, 30, 5, 5)); // 20行×30列のグリッドレイアウト
        labels = new JLabel[600]; // 最大600文字のラベルを用意
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel("", SwingConstants.CENTER);
            labels[i].setFont(new Font("Serif", Font.BOLD, 24));
            labels[i].setOpaque(true); // 背景色を変えるため
            labels[i].setPreferredSize(new Dimension(30, 30));
            centerPanel.add(labels[i]);
        }

        JScrollPane scrollPane = new JScrollPane(centerPanel); // スクロール可能に
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // クリックイベントを追加して、色を変える動作を有効にする
        centerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                revealMistake();
            }
        });

        setVisible(true);
    }

    // 「生成」ボタンをクリック時
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateButton) {
            int charCount;
            try {
                charCount = Integer.parseInt(inputField.getText());
                if (charCount < 2 || charCount > 600) { // 最大600文字に制限
                    JOptionPane.showMessageDialog(this, "2〜600の範囲で文字数を入力してください");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "数値を入力してください");
                return;
            }

            generateCharacters(charCount);
        }
    }

    // 文字列を生成して表示
    private void generateCharacters(int count) {
        revealed = false; // 初期化
        mistakeIndex = random.nextInt(count); // 間違いの位置をランダムに設定

        // ランダムに似ている文字ペアを選択
        String[] pair = SIMILAR_PAIRS[random.nextInt(SIMILAR_PAIRS.length)];
        String mainChar = pair[0];
        String mistakeChar = pair[1];

        // ラベルに文字を設定
        for (int i = 0; i < count; i++) {
            labels[i].setText(i == mistakeIndex ? mistakeChar : mainChar);
            labels[i].setBackground(Color.WHITE); // 背景色を初期化
            labels[i].setForeground(Color.BLACK); // 文字色を初期化
        }
        for (int i = count; i < labels.length; i++) {
            labels[i].setText(""); // 残りは非表示
        }
    }

    // 間違い文字を色変えして強調表示
    private void revealMistake() {
        if (!revealed) {
            labels[mistakeIndex].setBackground(Color.YELLOW); // 間違い文字の背景色を黄色に
            revealed = true; // 一度表示したら再表示しないようフラグを設定
        } else {
            labels[mistakeIndex].setBackground(Color.WHITE); // 色を元に戻す
            revealed = false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReportAA());
    }
}
