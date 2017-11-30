/**
 * Copyright 2010 - 2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrains.exodus.crypto.streamciphers

import jetbrains.exodus.crypto.StreamCipher
import jetbrains.exodus.crypto.StreamCipherProvider
import jetbrains.exodus.crypto.toByteArray
import org.bouncycastle.crypto.engines.ChaCha7539Engine
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.crypto.params.ParametersWithIV

const val CHACHA_CIPHER_ID = "jetbrains.exodus.crypto.streamciphers.ChaChaStreamCipherProvider"

// ChaCha initialization vector is 12 bytes
private const val CHACHA_IV_SIZE = 12

/**
 * ChaCha stream cipher with 20 rounds. Respects [RFC-7539](https://tools.ietf.org/html/rfc7539 RFC-7539).
 */
@Suppress("unused")
class ChaChaStreamCipherProvider : StreamCipherProvider() {

    override fun getId() = CHACHA_CIPHER_ID

    override fun newCipher(): StreamCipher = Salsa20StreamCipher()

    private class Salsa20StreamCipher : StreamCipher {

        private lateinit var engine: ChaCha7539Engine

        override fun init(key: ByteArray, iv: Long) {
            this.engine = ChaCha7539Engine().apply {
                init(true, ParametersWithIV(KeyParameter(key), iv.toByteArray(CHACHA_IV_SIZE)))
            }
        }

        override fun crypt(b: Byte): Byte {
            return engine.returnByte(b)
        }
    }
}