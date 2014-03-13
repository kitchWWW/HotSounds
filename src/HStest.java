import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;




public class HStest {
	public HStest(){
		JFrame m = new JFrame("Why hello there");
		m.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		m.setVisible(true);
		
	}
}
