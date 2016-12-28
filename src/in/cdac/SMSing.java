package in.cdac;
import java.io.*;
import java.net.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.Color;

public class SMSing extends javax.swing.JFrame {

		private javax.swing.JLabel messageLable;
		private javax.swing.JLabel mobileNumbersLable;
		private javax.swing.JButton sendButton;
		private javax.swing.JLabel responseLable;
		private javax.swing.JTextArea messageTextField;
		private javax.swing.JTextArea numbersTextField;

		private String username = "";
		private String password = "";
		private String senderId = "";

		public SMSing() {
			setResizable(false);
			initComponents();
		}

		private void initComponents() {

				messageTextField = new javax.swing.JTextArea(5,20);
				
				numbersTextField = new javax.swing.JTextArea(3,20);
				messageLable = new javax.swing.JLabel();
				mobileNumbersLable = new javax.swing.JLabel();
				sendButton = new javax.swing.JButton();

				responseLable = new javax.swing.JLabel();
				responseLable.setForeground(new Color(0, 128, 0));
				setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

				setTitle("SMSing Utility For GATE");

				messageLable.setText("Message");
				mobileNumbersLable.setText("Mobile Number(s)");
				sendButton.setText("Send Message");

				sendButton.addActionListener( new java.awt.event.ActionListener() {
								public void actionPerformed( java.awt.event.ActionEvent evt ) {
								sendButtonActionPerformed(evt);
								}
								});
				
				JLabel lblNoteIfYou = new JLabel();
				lblNoteIfYou.setText("Note: If you have multiple mobile numbers please use comma or space as a separator");


				javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane() );
				layout.setHorizontalGroup(
					layout.createParallelGroup(Alignment.TRAILING)
						.addGroup(layout.createSequentialGroup()
							.addGap(12)
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
									.addGap(22)
									.addComponent(messageLable))
								.addGroup(layout.createSequentialGroup()
									.addComponent(mobileNumbersLable)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
											.addComponent(messageTextField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
											.addComponent(sendButton, Alignment.LEADING)
											.addGroup(Alignment.LEADING, layout.createSequentialGroup()
												.addGap(12)
												.addComponent(responseLable, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE))
											.addComponent(numbersTextField, Alignment.LEADING))
										.addComponent(lblNoteIfYou, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 665, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)))
							.addContainerGap())
				);
				layout.setVerticalGroup(
					layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
							.addGap(6)
							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(messageLable)
								.addComponent(messageTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
									.addGap(12)
									.addComponent(mobileNumbersLable))
								.addGroup(layout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(numbersTextField, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(responseLable)
							.addGap(9)
							.addComponent(lblNoteIfYou)
							.addGap(18)
							.addComponent(sendButton)
							.addContainerGap())
				);
				getContentPane().setLayout( layout );
				pack();
		}

		private HttpURLConnection sendSingleSMS(String mobileNo, String message, HttpURLConnection connection) {

				try {
						String smsservicetype = "singlemsg"; // For single message.
						String query = "username=" + URLEncoder.encode(username,"UTF-8")
								+ "&password=" + URLEncoder.encode(password,"UTF-8")
								+ "&smsservicetype=" + URLEncoder.encode(smsservicetype,"UTF-8")
								+ "&content=" + URLEncoder.encode(message,"UTF-8") + "&mobileno="
								+ URLEncoder.encode(mobileNo,"UTF-8") + "&senderid="
								+ URLEncoder.encode(senderId,"UTF-8");

						connection.setRequestProperty("Content-length", String.valueOf(query.length()));
						connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
						connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)");

						DataOutputStream output = new DataOutputStream(connection.getOutputStream());
						
						int queryLength = query.length();
						output.writeBytes(query);
						output.close();
						DataInputStream input = new DataInputStream(connection .getInputStream());
						for (int c = input.read(); c != -1; c = input.read())
								System.out.print((char) c);
						input.close();

				} catch (Exception e) {
						e.printStackTrace();
				}
				return connection;
		}

		private HttpURLConnection getConnection(){
				HttpURLConnection connection = null;
				try{
						URL url = new URL("");
						connection = (HttpURLConnection) url.openConnection();
						System.out.println("Connection has been opened");
						connection.setDoInput(true);
						connection.setDoOutput(true);
						connection.setRequestMethod("POST");
						connection.setFollowRedirects(true);

				}catch(Exception e){
						e.printStackTrace();
				}
				return connection;
		} 

		private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) { 

				try{
						responseLable.setText("Message Sending... ");
						String message  = messageTextField.getText();
						String mobileNos  = numbersTextField.getText();
						int count = 0;
						String[] numbers = mobileNos.split("\\W+");	
						HttpURLConnection connection = null;
						
						for( String number: numbers ){	
							try{
								connection = getConnection();
								connection = sendSingleSMS( number, message, connection);
								count++;
							}catch(Exception e){
								responseLable.setText("Error in sending SMS: only "+count+" Messages sent");
								return;
							
							}
						}	
						responseLable.setText(count+" Messages sent");
						messageTextField.setText("");
						numbersTextField.setText("");
				} catch (Exception e) {
						e.printStackTrace();
				}
		}


		public static void main(String args[]) {
				java.awt.EventQueue.invokeLater(new Runnable() {
								public void run() {
								new SMSing().setVisible(true);
								}});
		}
}
