/**
 *  Passman Android App
 *
 * @copyright Copyright (c) 2016, Sander Brand (brantje@gmail.com)
 * @copyright Copyright (c) 2016, Marcos Zuriaga Miguel (wolfi@wolfi.es)
 * @license GNU AGPL version 3 or any later version
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package es.wolfi.app.passman;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.wolfi.passman.API.Vault;
import es.wolfi.utils.GeneralUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VaultUnlockInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VaultLockScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VaultLockScreen extends Fragment {
    private Vault vault;

    private VaultUnlockInteractionListener mListener;

    @BindView(R.id.fragment_vault_name)
    TextView vault_name;
    @BindView(R.id.fragment_vault_password)
    EditText vault_password;
    @BindView(R.id.fragment_vault_unlock)
    Button btn_unlock;
    @BindView(R.id.vault_lock_screen_chk_save_pw)
    CheckBox chk_save;

    public VaultLockScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param vault The vault
     * @return A new instance of fragment VaultLockScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static VaultLockScreen newInstance(Vault vault) {
        VaultLockScreen fragment = new VaultLockScreen();
        fragment.vault = vault;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vault_lock_screen, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof VaultUnlockInteractionListener) {
            mListener = (VaultUnlockInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement VaultUnlockInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        vault = Vault.getActiveVault();

        Log.e("VaultLockScreen", "Vault guid: ".concat(vault.guid));
        vault_name.setText(vault.name);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.fragment_vault_unlock)
    void onBtnUnlockClick() {
        GeneralUtils.debug("Unlocking Vault");
        if (vault.unlock(vault_password.getText().toString())) {
            if (chk_save.isChecked()) {
                SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getContext());
                p.edit().putString(vault.guid, vault_password.getText().toString()).commit();
            }
            mListener.onVaultUnlock(vault);
            return;
        }

        GeneralUtils.toast(getView(), getString(R.string.wrong_vault_pw));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface VaultUnlockInteractionListener {
        void onVaultUnlock(Vault vault);
    }
}
